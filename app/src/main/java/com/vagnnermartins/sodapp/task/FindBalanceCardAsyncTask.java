package com.vagnnermartins.sodapp.task;

import android.os.AsyncTask;

import com.vagnnermartins.sodapp.callback.Callback;
import com.vagnnermartins.sodapp.dto.BalanceDTO;
import com.vagnnermartins.sodapp.model.Card;
import com.vagnnermartins.sodapp.service.CardService;

/**
 * Created by vagnnermartins on 30/01/15.
 */
public class FindBalanceCardAsyncTask extends AsyncTask<Void, Void, BalanceDTO> {

    private final Card card;
    private final Callback callback;
    private final String hostName;
    private Exception exception;

    public FindBalanceCardAsyncTask(String hostName, Callback callback, Card card){
        this.hostName = hostName;
        this.callback = callback;
        this.card = card;
    }

    @Override
    protected BalanceDTO doInBackground(Void... params) {
        BalanceDTO result = null;
        try {
            result = CardService.findBalanceByCard(hostName, card);
            if(result == null){
                exception = new Exception();
            }
        } catch (Exception e) {
            exception = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(BalanceDTO result) {
        super.onPostExecute(result);
        callback.onReturn(exception, result);
    }
}
