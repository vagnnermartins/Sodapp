package com.vagnnermartins.sodapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.ads.AdRequest;
import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.adapter.CardAdapter;
import com.vagnnermartins.sodapp.app.App;
import com.vagnnermartins.sodapp.constants.Keys;
import com.vagnnermartins.sodapp.model.Card;
import com.vagnnermartins.sodapp.ui.helper.MainUIHelper;
import com.vagnnermartins.sodapp.util.NavegacaoUtil;
import com.vagnnermartins.sodapp.util.trivial.IabHelper;
import com.vagnnermartins.sodapp.util.trivial.IabResult;
import com.vagnnermartins.sodapp.util.trivial.Purchase;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_NEW_CARD = 1;
    private static final int REQUEST_UPDATE_CARD = 2;
    private static final String REMOVER_ANUNCIOS = "remover_anuncio";

    private App app;
    private MainUIHelper ui;
    private Card selectedCard;
    private Card removedCard;
    private ActionMode.Callback mCallback;
    private ActionMode mMode;

    private IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadValues();
    }

    private void init() {
        app = (App) getApplication();
        ui = new MainUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        setSupportActionBar(ui.toolbar);
        ui.message.setOnClickListener(onMessageClickListener());
        ui.list.setOnItemClickListener(onItemClickListener());
        ui.list.setOnItemLongClickListener(onItemLongClickListener());
        configActionMode();
        initAdMob();
    }

    private void initAdMob() {
//        AdRequest adRequestTeste = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")
//                .build();
        AdRequest adRequestProd = new AdRequest.Builder().build();
        ui.adView.loadAd(adRequestProd);
    }

    private void loadValues() {
        if(app.myCards.isEmpty()){
            ui.message.setVisibility(View.VISIBLE);
        }else{
            ui.message.setVisibility(View.GONE);
        }
        ui.list.setAdapter(new CardAdapter(this, R.layout.item_card, new ArrayList<>(app.myCards)));
    }

    private AdapterView.OnItemLongClickListener onItemLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                closeActionMode();
                selectedCard = (Card) parent.getItemAtPosition(position);
                if(mMode != null){
                    return false;
                }else{
                    mMode = startSupportActionMode(mCallback);
                }
                return true;
            }
        };
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card selected = (Card) parent.getItemAtPosition(position);
                if(mMode == null || selected == selectedCard){
                    app.selectedCard = selected;
                    startBalance();
                }
                closeActionMode();
            }
        };
    }

    private void configActionMode(){
        mCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.toolbar).setVisibility(View.GONE);
                actionMode.setTitle(selectedCard.getName());
                getMenuInflater().inflate(R.menu.context_menu_card, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.context_menu_card_delete:
                        removedCard = selectedCard;
                        app.myCards.remove(selectedCard);
                        SnackBar snack = new SnackBar(MainActivity.this, getString(R.string.cards_activity_delete_message), getString(R.string.undo), onUndoClickListener());
                        snack.setOnhideListener(onHideListener());
                        snack.show();
                        loadValues();
                        break;
                    case R.id.context_menu_card_edit:
                        app.selectedCard = selectedCard;
                        NavegacaoUtil.startWithResult(MainActivity.this, NewCardActivity.class, REQUEST_UPDATE_CARD);
                        break;
                }
                actionMode.finish();
                return false;
            }

            private SnackBar.OnHideListener onHideListener() {
                return new SnackBar.OnHideListener() {
                    @Override
                    public void onHide() {
                        if(removedCard != null){
                            removedCard.unpinInBackground();
                        }
                    }
                };
            }

            private View.OnClickListener onUndoClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        app.myCards.add(removedCard);
                        loadValues();
                        removedCard = null;
                    }
                };
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                mMode = null;
                getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            }
        };
    }

    private View.OnClickListener onMessageClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavegacaoUtil.startWithResult(MainActivity.this, NewCardActivity.class, REQUEST_NEW_CARD);
            }
        };
    }

    private void startBalance() {
        if(app.isInternetConnection(this)){
            NavegacaoUtil.start(this, BalanceCardActivity.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                NavegacaoUtil.startWithResult(MainActivity.this, NewCardActivity.class, REQUEST_NEW_CARD);
                break;
            case R.id.menu_remove_ad:
                mHelper = new IabHelper(this, Keys.BASE_64);
                mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                    @Override
                    public void onIabSetupFinished(IabResult result) {
                        mHelper.launchPurchaseFlow(MainActivity.this, "remover_anuncio", 10001,
                        new IabHelper.OnIabPurchaseFinishedListener() {
                            @Override
                            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                                Toast.makeText(MainActivity.this, "Finish", Toast.LENGTH_SHORT).show();
                                if (result.isFailure()) {
                                    Toast.makeText(MainActivity.this, "Falha " + result.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (result.isSuccess() && info.getSku().equals(REMOVER_ANUNCIOS)) {
                                    Toast.makeText(MainActivity.this, "Removido " + result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeActionMode(){
        if(mMode != null){
            mMode.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_NEW_CARD:
                    new SnackBar(this, getString(R.string.new_card_activity_save_success)).show();
                    break;
                case REQUEST_UPDATE_CARD:
                    new SnackBar(this, getString(R.string.new_card_activity_updated_success)).show();
            }
            loadValues();
        }
        app.selectedCard = null;
    }

}