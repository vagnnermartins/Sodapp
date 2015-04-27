package com.vagnnermartins.sodapp.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.dto.BalanceDTO;
import com.vagnnermartins.sodapp.ui.view.GeneralSwipeRefreshLayout;

/**
 * Created by vagnnermartins on 03/02/15.
 */
public class BalanceCardUIHelper {

    public Toolbar toolbar;
    public ListView list;
    public AdView adView;
    public View progress;
    public View message;
    public GeneralSwipeRefreshLayout swipeLayout;

    public BalanceCardUIHelper(View view){
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        this.list = (ListView) view.findViewById(R.id.balance_card_list);
        this.adView = (AdView) view.findViewById(R.id.balance_card_adview);
        this.progress = view.findViewById(R.id.balance_card_progress);
        this.message = view.findViewById(R.id.balance_card_message);
        this.swipeLayout = (GeneralSwipeRefreshLayout) view.findViewById(R.id.balance_card_swipe);
        this.swipeLayout.setListView(list);
    }

    public View setListViewHeader(View view, BalanceDTO balance){
        setTextViewValue(view, R.id.balance_card_name, balance.getName());
        setTextViewValue(view, R.id.balance_card_enterprise, balance.getEnterprise());
        setTextViewValue(view, R.id.balance_card_card, balance.getCard());
        setTextViewValue(view, R.id.balance_card_value, "Saldo atual: " + balance.getValue());
        setTextViewValue(view, R.id.balance_card_message, balance.getMessageBalance());
        return view;
    }

    private void setTextViewValue(View view, int resource, String value){
        ((TextView) view.findViewById(resource)).setText(value);
    }
}
