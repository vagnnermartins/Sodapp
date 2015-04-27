package com.vagnnermartins.sodapp.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.vagnnermartins.sodapp.R;

/**
 * Created by vagnnermartins on 29/01/15.
 */
public class MainUIHelper {

    public Toolbar toolbar;
    public TextView message;
    public ListView list;
    public AdView adView;

    public MainUIHelper(View view){
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        this.list = (ListView) view.findViewById(R.id.main_list);
        this.message = (TextView) view.findViewById(R.id.main_message);
        this.adView = (AdView) view.findViewById(R.id.main_adview);
    }
}
