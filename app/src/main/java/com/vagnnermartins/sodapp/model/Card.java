package com.vagnnermartins.sodapp.model;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.vagnnermartins.sodapp.enums.CardTypeEnum;

/**
 * Created by vagnnermartins on 29/01/15.
 */
@ParseClassName("Card")
public class Card extends ParseObject {

    private static final String NAME = "name";
    private static final String CPF = "cpf";
    private static final String TYPE = "type";
    private static final String NUMBER = "number";
    private static final String DATE = "date";
    private static final String VALUE = "value";

    public static void findMyCards(FindCallback<Card> callback){
        ParseQuery<Card> query = ParseQuery.getQuery(Card.class);
        query.orderByDescending(NAME);
        query.fromLocalDatastore();
        query.findInBackground(callback);
    }

    public void setName(String name){
        put(NAME, name);
    }
    public String getName(){
        return getString(NAME);
    }
    public void setCpf(String cpf){
        put(CPF, cpf);
    }
    public String getCpf(){
        return getString(CPF);
    }
    public void setNumber(String number){
        put(NUMBER, number);
    }
    public String getNumber(){
        return getString(NUMBER);
    }
    public void setType(String type){
        put(TYPE, type);
    }
    public void setValue(String value){
        put(VALUE, value);
    }
    public String getValue(){
        return getString(VALUE);
    }
    public void setDate(String date){
        put(DATE, date);
    }
    public String getDate(){
        return getString(DATE);
    }
    public CardTypeEnum getType(){
        String type = getString(TYPE);
        return CardTypeEnum.valueOf(type.toUpperCase());
    }

    @Override
    public String toString() {
        return getObjectId();
    }
}
