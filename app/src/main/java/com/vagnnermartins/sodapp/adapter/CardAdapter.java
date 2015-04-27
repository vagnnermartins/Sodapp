package com.vagnnermartins.sodapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vagnnermartins.sodapp.R;
import com.vagnnermartins.sodapp.model.Card;

import java.util.List;


/**
 * Created by vagnnermartins on 29/01/15.
 */
public class CardAdapter extends ArrayAdapter<Card> {

    public CardAdapter(Context context, int resource, List<Card> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = parent.inflate(getContext(), R.layout.item_card, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Card item = getItem(position);
        viewHolder.date.setText(item.getDate());
        viewHolder.value.setText(item.getValue());
        viewHolder.image.setImageResource(item.getType().getImage());
        viewHolder.name.setText(item.getName());
        if(item.getDate() == null){
            viewHolder.value.setText(R.string.cards_activity_no_balance);
            viewHolder.last.setVisibility(View.GONE);
        }else{
            viewHolder.last.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder{

        ImageView image;
        TextView name;
        TextView date;
        TextView value;
        TextView last;

        public ViewHolder(View view){
            this.image = (ImageView) view.findViewById(R.id.item_card_image);
            this.name = (TextView) view.findViewById(R.id.item_card_name);
            this.date = (TextView) view.findViewById(R.id.item_card_date);
            this.value = (TextView) view.findViewById(R.id.item_card_value);
            this.last = (TextView) view.findViewById(R.id.item_card_last);
        }
    }
}
