package com.vagnnermartins.sodapp.enums;

import com.vagnnermartins.sodapp.R;

/**
 * Created by vagnnermartins on 29/01/15.
 */
public enum CardTypeEnum {

    REFEICAO_PASS("Refeição Pass", "5;1;6", R.drawable.image_refeicao_pass),
    ALIMENTACAO_PASS("Alimentação Pass","5;2;4", R.drawable.image_alimentacao_pass),
    GIFT_PASS("Gift Pass", "5;26;4", R.drawable.image_gift_pass),
    PREMIUM_PASS("Premium Pass", "5;25;4", R.drawable.image_premium_pass),
    COMBUSTIVEL_PASS("Combustível Pass", "5;30;4", R.drawable.image_combustivel_pass),
    ALIMENTACAO_PASS_NATAL("Alimentação Pass Natal", "5;026;4", R.drawable.image_alimentacao_pass_natal),
    CULTURA("Vale-Cultura da Sodexo", "5;27;4", R.drawable.image_vale_cultura),
    BRINQUEDO_PASS("Brinquedo Pass", "5;0026;4", R.drawable.image_brinquedo);

    private final String description;
    private final String identifier;
    private final int image;

    private CardTypeEnum(String description, String identifier, int image){
        this.description = description;
        this.identifier = identifier;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getImage() {
        return image;
    }

}
