package com.vagnnermartins.sodapp.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.app.App;
import com.vagnnermartins.sodapp.enums.CardTypeEnum;
import com.vagnnermartins.sodapp.enums.StatusEnum;
import com.vagnnermartins.sodapp.model.Card;
import com.vagnnermartins.sodapp.ui.helper.NewCardUIHelper;
import com.vagnnermartins.sodapp.util.KeyboardUtil;

import java.util.List;

public class NewCardActivity extends ActionBarActivity implements Validator.ValidationListener {

    private App app;
    private NewCardUIHelper ui;
    private CardTypeEnum chosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        init();
        checkValues();
    }

    private void init() {
        app = (App) getApplication();
        ui = new NewCardUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        setSupportActionBar(ui.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ui.saveButton.setOnClickListener(onSaveClickListener());
        ui.type.setOnClickListener(onTypeClickListener());
        ui.validator.setValidationListener(this);
        ui.cpf.setOnEditorActionListener(onEditorActionListener());
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

    private void checkValues(){
        if(app.selectedCard != null){
            ui.cpf.setText(app.selectedCard.getCpf());
            ui.cardNumber.setText(app.selectedCard.getNumber());
            ui.type.setText(app.selectedCard.getType().getDescription());
            ui.name.setText(app.selectedCard.getName());
            chosed = app.selectedCard.getType();
        }
    }

    private void checkStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            checkStatusInicio();
        }else if(status == StatusEnum.EXECUTANDO){
            checkStatusExecutando();
        }else if(status == StatusEnum.EXECUTADO){
            checkStatusExecutado();
        }
    }

    private void checkStatusInicio() {
        if(app.selectedCard == null){
            app.selectedCard = new Card();
        }
        app.selectedCard.setNumber(ui.cardNumber.getText().toString());
        app.selectedCard.setName(ui.name.getText().toString().toUpperCase());
        app.selectedCard.setCpf(ui.cpf.getText().toString());
        app.selectedCard.setType(chosed.toString());
        app.selectedCard.pinInBackground(onPinCallback());
        checkStatus(StatusEnum.EXECUTANDO);
    }

    private void checkStatusExecutando() {
        ui.saveButton.setEnabled(false);
        ui.saveButton.setAlpha(0.5f);
        ui.progress.setVisibility(View.VISIBLE);
    }

    private void checkStatusExecutado() {
        ui.saveButton.setEnabled(true);
        ui.saveButton.setAlpha(1f);
        ui.progress.setVisibility(View.GONE);
        app.myCards.add(app.selectedCard);
        setResult(RESULT_OK);
        finish();
    }

    private View.OnClickListener onSaveClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ui.validator.validate();
            }
        };
    }

    private View.OnClickListener onTypeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogType();
                KeyboardUtil.hideKeyboard(NewCardActivity.this, ui.type);
            }

        };
    }

    private void showDialogType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewCardActivity.this);
        CardTypeEnum[] types = CardTypeEnum.values();
        String[] options = new String[types.length];
        for(int i = 0; i < types.length; i++){
            options[i] = types[i].getDescription();
        }
        builder.setItems(options, onItemClickListener(types));
        builder.setTitle(R.string.new_card_activity_type_choose);
        builder.create().show();
        ui.type.requestFocus();
    }

    private DialogInterface.OnClickListener onItemClickListener(final CardTypeEnum[] types) {
        return new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                chosed = types[which];
                ui.type.setText(chosed.getDescription());
            }
        };
    }

    @Override
    public void onValidationSucceeded() {
        checkStatus(StatusEnum.INICIO);
    }

    private SaveCallback onPinCallback() {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    checkStatus(StatusEnum.EXECUTADO);
                }
            }
        };
    }

    private TextView.OnEditorActionListener onEditorActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    showDialogType();
                    KeyboardUtil.hideKeyboard(NewCardActivity.this, ui.cpf);
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            error.getView().requestFocus();
            ((EditText) error.getView()).setError(error.getFailedRules().get(0).getMessage(this));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(app.showInterstitial == 1 || app.showInterstitial % 4 == 0){
            AdBuddiz.showAd(this);
        }
        app.showInterstitial++;
    }
}
