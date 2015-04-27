package com.vagnnermartins.sodapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.constants.SodexoValues;
import com.vagnnermartins.sodapp.dto.ItemBalanceDTO;

import java.util.List;

/**
 * Created by vagnnermartins on 29/01/15.
 */
public class BalanceAdapter extends ArrayAdapter<ItemBalanceDTO> {

    public BalanceAdapter(Context context, int resource, List<ItemBalanceDTO> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = parent.inflate(getContext(), R.layout.item_balance, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ItemBalanceDTO item = getItem(position);
        viewHolder.place.setText(item.getPlace());
        viewHolder.value.setText(item.getValue());
        viewHolder.date.setText(item.getDate());
        if(item.getType().equals(SodexoValues.CREDIT)){
            viewHolder.value.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        }else{
            viewHolder.value.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));
        }
        return convertView;
    }

    class ViewHolder{

        TextView place;
        TextView date;
        TextView value;

        public ViewHolder(View view){
            this.place = (TextView) view.findViewById(R.id.item_balance_place);
            this.value = (TextView) view.findViewById(R.id.item_balance_value);
            this.date = (TextView) view.findViewById(R.id.item_balance_date);
        }
    }
}
