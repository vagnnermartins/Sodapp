package com.vagnnermartins.sodapp.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdView;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Size;

import com.vagnnermartins.sodapp.R;

/**
 * Created by vagnnermartins on 29/01/15.
 */
public class NewCardUIHelper {

    public Validator validator;
    public Toolbar toolbar;
    public AdView adView;

    @NotEmpty(messageResId = R.string.new_card_activity_name_validate, sequence = 1)
    public EditText name;

    @Size(min = 16, messageResId = R.string.new_card_activity_number_validate)
    @NotEmpty(messageResId = R.string.new_card_activity_number_validate, sequence = 2)
    public EditText cardNumber;

    @Size(min = 11, messageResId = R.string.new_card_activity_cpf_validate)
    @NotEmpty(messageResId = R.string.new_card_activity_cpf_validate, sequence = 3)
    public EditText cpf;

    @Size(messageResId = R.string.new_card_activity_type_validate)
    @NotEmpty(messageResId = R.string.new_card_activity_type_validate, sequence = 4)
    public EditText type;

    public View progress;
    public View saveButton;

    public NewCardUIHelper(View view){
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        this.name = (EditText) view.findViewById(R.id.new_card_name);
        this.cardNumber = (EditText) view.findViewById(R.id.new_card_card_number);
        this.cpf = (EditText) view.findViewById(R.id.new_card_cpf);
        this.type = (EditText) view.findViewById(R.id.new_card_type);
        this.saveButton = view.findViewById(R.id.new_card_save);
        this.progress = view.findViewById(R.id.new_card_progress);
        this.adView = (AdView) view.findViewById(R.id.new_card_adview);
        this.validator = new Validator(this);
    }

}
