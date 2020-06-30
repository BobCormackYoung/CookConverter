package com.youngsoft.cookconverter.ui.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.youngsoft.cookconverter.data.IngredientsRecord;

public class IngredientsSpinnerAdapter extends ArrayAdapter<IngredientsRecord> {

    //array of spinner values
    private final IngredientsRecord[] values;

    public IngredientsSpinnerAdapter(Context context, IngredientsRecord[] values) {
        super(context, 0, values);
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public IngredientsRecord getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // Passive spinner state
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initConvertView(position, convertView, parent);
    }

    // When picker displayed
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initConvertView(position, convertView, parent);
    }

    /**
     * create & return the view for displaying the data
     * @param position the position of of the item in the list
     * @param convertView the view that needs creating
     * @param parent the holder for the view that will be displayed
     * @return the view that is created
     */
    private View initConvertView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        IngredientsRecord currentItem = getItem(position);

        if (currentItem != null) {
            textView.setText(currentItem.getName());
        }

        return convertView;
    }

}
