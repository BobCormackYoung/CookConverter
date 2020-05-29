package com.youngsoft.cookconverter.ui.measures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.IngredientsRecord;

public class IngredientsSpinnerAdapter extends ArrayAdapter<IngredientsRecord> {

    //TODO: https://codinginflow.com/tutorials/android/custom-spinner
    //TODO: https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list

    // Your sent context
    private Context context;

    // Your custom values for the spinner (User)
    private IngredientsRecord[] values;

    public IngredientsSpinnerAdapter(Context context, IngredientsRecord[] values) {
        super(context, 0, values);
        this.context = context;
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


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
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
