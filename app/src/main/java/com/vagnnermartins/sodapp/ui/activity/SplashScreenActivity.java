package com.vagnnermartins.sodapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.app.App;
import com.vagnnermartins.sodapp.constants.Keys;
import com.vagnnermartins.sodapp.enums.StatusEnum;
import com.vagnnermartins.sodapp.model.Card;
import com.vagnnermartins.sodapp.util.NavegacaoUtil;

import java.util.List;

public class SplashScreenActivity extends ActionBarActivity {

    private App app;
    private SnackBar snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
        checkStatus(StatusEnum.INICIO);
    }

    private void init() {
        app = (App) getApplication();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        AdBuddiz.setPublisherKey(Keys.AD_BUDDIZ);
        AdBuddiz.cacheAds(this);
    }

    private void checkStatus(StatusEnum status) {
        if(status == StatusEnum.INICIO){
            Card.findMyCards(onFindMyCards());
        }else if(status == StatusEnum.EXECUTADO){
            NavegacaoUtil.start(this, MainActivity.class);
            finish();
        }
    }

    private FindCallback<Card> onFindMyCards() {
        return new FindCallback<Card>() {
            @Override
            public void done(List<Card> result, ParseException e) {
                if(e == null){
                    app.myCards.addAll(result);
                    checkStatus(StatusEnum.EXECUTADO);
                }else{
                    showErrorMessage(getString(R.string.splash_activity_error_find_my_cards));
                }
            }
        };
    }

    private void showErrorMessage(String message) {
        snack = new SnackBar(SplashScreenActivity.this,
                message,
                getString(R.string.try_again), onTryAgainClickListener());
        snack.show();
    }

    private View.OnClickListener onTryAgainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(StatusEnum.INICIO);
                snack.dismiss();
            }
        };
    }
}
