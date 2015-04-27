package com.vagnnermartins.sodapp.service;

import com.android.vending.billing.IInAppBillingService;
import com.vagnnermartins.sodapp.constants.Enviroment;
import com.vagnnermartins.sodapp.dto.BalanceDTO;
import com.vagnnermartins.sodapp.model.Card;
import com.vagnnermartins.sodapp.util.ServiceUtil;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

/**
 * Created by vagnnermartins on 30/01/15.
 */
public class CardService {

    public static BalanceDTO findBalanceByCard(String hostname, Card card) throws Exception {
        BalanceDTO result = null;
        String path = String.format(Enviroment.FIND_BALANCE, hostname, card.getCpf(), card.getType().getIdentifier(), card.getNumber());
        HttpResponse response = ServiceUtil.executarGet(path);
        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_INTERNAL_SERVER_ERROR){
            result = BalanceDTO.fromJsonToObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        }
        return result;
    }
}
