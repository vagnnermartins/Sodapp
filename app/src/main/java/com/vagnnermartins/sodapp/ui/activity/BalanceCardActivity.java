package com.vagnnermartins.sodapp.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.ads.AdRequest;
import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.adapter.BalanceAdapter;
import com.vagnnermartins.sodapp.app.App;
import com.vagnnermartins.sodapp.callback.Callback;
import com.vagnnermartins.sodapp.constants.Enviroment;
import com.vagnnermartins.sodapp.dto.BalanceDTO;
import com.vagnnermartins.sodapp.enums.StatusEnum;
import com.vagnnermartins.sodapp.task.FindBalanceCardAsyncTask;
import com.vagnnermartins.sodapp.ui.helper.BalanceCardUIHelper;
import com.vagnnermartins.sodapp.util.DataUtil;

import java.util.Date;

public class BalanceCardActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private App app;
    private BalanceCardUIHelper ui;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_card);
        init();
        checkUpdate();
    }

    private void checkUpdate() {
        if(app.balance == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            setList();
        }
    }

    private void init() {
        app = (App) getApplication();
        ui = new BalanceCardUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        ui.swipeLayout.setOnRefreshListener(this);
        ui.swipeLayout.setColorSchemeResources(R.color.primary_dark);
        setSupportActionBar(ui.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app.selectedCard.getName());
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

    private void checkStatus(StatusEnum status) {
        if(status == StatusEnum.INICIO){
            checkStatusInicio();
        }else if(status == StatusEnum.EXECUTANDO){
            checkStatusExecutando();
        }else if(status == StatusEnum.EXECUTADO){
            checkStatusExecutado();
        }
    }

    private void checkStatusInicio() {
        if(app.isInternetConnection(this)){
            if(app.hostName == null){
                findHostname();
            }else{
                findBalance();
            }
            checkStatus(StatusEnum.EXECUTANDO);
        }
    }

    private void findHostname() {
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, ParseException e) {
                if (e == null) {
                    app.hostName = parseConfig.getString(Enviroment.PATH);
                    checkStatus(StatusEnum.INICIO);
                }else{
                    showMessageError(getString(R.string.balance_card_error));
                }
            }
        });
    }

    private void findBalance() {
        FindBalanceCardAsyncTask task = new FindBalanceCardAsyncTask(app.hostName, onFindBalanceCallback(), app.selectedCard);
        task.execute();
        app.register(task);
    }

    private void checkStatusExecutando() {
        if(ui.list.getAdapter() == null ){
            ui.progress.setVisibility(View.VISIBLE);
        }else{
            ui.swipeLayout.setRefreshing(true);
        }
    }

    private void checkStatusExecutado() {
        ui.swipeLayout.setRefreshing(false);
        ui.progress.setVisibility(View.GONE);
    }

    private Callback onFindBalanceCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                app.balance = (BalanceDTO) objects[0];
                if(error == null){
                    if(app.balance.getMessage() == null){
                        setList();
                        saveCard();
                    }else{
                        showMessageError(app.balance.getMessage());
                    }
                }else{
                    showMessageError(getString(R.string.balance_card_error));
                }
                checkStatus(StatusEnum.EXECUTADO);
            }

            private void saveCard() {
                if(app.balance != null && app.selectedCard != null){
                    app.selectedCard.setValue(app.balance.getValue());
                    app.selectedCard.setDate(DataUtil.transformDateToSting(new Date(), "dd/MM/yyyy"));
                    app.selectedCard.pinInBackground();
                }
            }

        };
    }

    private void setList(){
        ui.list.setAdapter(new BalanceAdapter(this, R.layout.item_balance, app.balance.getTransactions()));
        if(header == null){
            header = getLayoutInflater().inflate(R.layout.header_balance_card, null);
            ui.list.addHeaderView(ui.setListViewHeader(header, app.balance));
        }else{
            ui.setListViewHeader(header, app.balance);
        }
        checkStatusExecutado();
    }

    private void showMessageError(String message) {
        if(!isFinishing()){
            SnackBar snack = new SnackBar(BalanceCardActivity.this, message,
                    getString(R.string.try_again), onTryAgainClickListener());
            snack.setIndeterminate(true);
            snack.show();
        }
    }

    private View.OnClickListener onTryAgainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(StatusEnum.INICIO);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                app.balance = null;
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        checkStatus(StatusEnum.INICIO);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(app.showInterstitial == 1 || app.showInterstitial % 5 == 0){
            AdBuddiz.showAd(this);
        }
        app.showInterstitial++;
    }
}
