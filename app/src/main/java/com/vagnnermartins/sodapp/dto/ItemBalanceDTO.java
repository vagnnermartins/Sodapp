package com.vagnnermartins.sodapp.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vagnnermartins on 03/02/15.
 */
public class ItemBalanceDTO {

    private static final String DATE = "date";
    private static final String VALUE = "value";
    private static final String TYPE = "type";
    private static final String PLACE = "place";

    private String date;
    private String value;
    private String type;
    private String place;

    public static ItemBalanceDTO fromJsonToObject(JSONObject json) throws JSONException {
        ItemBalanceDTO balance = new ItemBalanceDTO();
        balance.setDate(json.getString(DATE));
        balance.setValue(json.getString(VALUE));
        balance.setType(json.getString(TYPE));
        balance.setPlace(json.getString(PLACE));
        return balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
