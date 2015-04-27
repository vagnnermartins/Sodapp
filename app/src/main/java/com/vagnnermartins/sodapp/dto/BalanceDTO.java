package com.vagnnermartins.sodapp.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagnnermartins on 03/02/15.
 */
public class BalanceDTO {

    private static final String CARD = "card";
    private static final String NAME = "name";
    private static final String ENTERPRISE = "enterprise";
    private static final String VALUE = "value";
    private static final String STATUS = "status";
    private static final String MESSAGE_BALANCE = "messageBalance";
    private static final String TRANSACTIONS = "transactions";
    private static final String MESSAGE = "message";

    private String card;
    private String name;
    private String enterprise;
    private String value;
    private String status;
    private String messageBalance;
    private String message;
    private List<ItemBalanceDTO> transactions;

    public static BalanceDTO fromJsonToObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        BalanceDTO result = new BalanceDTO();
        result.setCard(obj.isNull(CARD) ? null : obj.getString(CARD));
        result.setName(obj.isNull(NAME) ? null :obj.getString(NAME));
        result.setEnterprise(obj.isNull(ENTERPRISE) ? null : obj.getString(ENTERPRISE));
        result.setValue(obj.isNull(VALUE) ? null : obj.getString(VALUE));
        result.setStatus(obj.isNull(STATUS) ? null : obj.getString(STATUS));
        result.setMessage(obj.isNull(MESSAGE) ? null : obj.getString(MESSAGE));
        result.setMessageBalance(obj.isNull(MESSAGE_BALANCE) ? null : obj.getString(MESSAGE_BALANCE));
        result.setTransactions(new ArrayList<ItemBalanceDTO>());
        if(!obj.isNull(MESSAGE_BALANCE)){
            JSONArray array = obj.getJSONArray(TRANSACTIONS);
            for (int i = 0; array != null && i < array.length(); i++) {
                result.getTransactions().add(ItemBalanceDTO.fromJsonToObject(obj.getJSONArray(TRANSACTIONS).getJSONObject(i)));
            }
        }
        return result;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageBalance() {
        return messageBalance;
    }

    public void setMessageBalance(String messageBalance) {
        this.messageBalance = messageBalance;
    }

    public List<ItemBalanceDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<ItemBalanceDTO> transactions) {
        this.transactions = transactions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
