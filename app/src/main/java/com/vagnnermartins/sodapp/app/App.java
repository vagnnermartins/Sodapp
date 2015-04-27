package com.vagnnermartins.sodapp.app;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.Parse;
import com.parse.ParseObject;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.constants.Keys;
import com.vagnnermartins.sodapp.dto.BalanceDTO;
import com.vagnnermartins.sodapp.model.Card;
import com.vagnnermartins.sodapp.util.ConnectionDetectorUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vagnnermartins on 29/01/15.
 */
public class App extends Application {

    public Set<Card> myCards;
    public Card selectedCard;
    private List<AsyncTask<?,?,?>> tasks;
    public BalanceDTO balance;
    public int showInterstitial = 1;
    public String hostName;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initParse();
        myCards = new HashSet<>();
        tasks = new ArrayList<>();
    }

    private void initParse() {
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Card.class);
        Parse.initialize(this, Keys.PARSE_APP_ID, Keys.PARSE_CLIENT_KEY);
    }

    public void register(AsyncTask task){
        tasks.add(task);
    }

    public void unregister(AsyncTask task){
        tasks.remove(task);
    }

    public boolean isInternetConnection(ActionBarActivity activity){
        ConnectionDetectorUtils cd = new ConnectionDetectorUtils(this);
        if (!cd.isConnectingToInternet()) {
            new SnackBar(activity, activity.getString(R.string.exception_erro_err_internet_disconnected)).show();
            return false;
        }
        return true;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for(AsyncTask task : tasks){
            if(!task.isCancelled()){
                task.cancel(true);
            }
            unregister(task);
        }
    }
}
