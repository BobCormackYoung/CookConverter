package com.youngsoft.cookconverter.ui.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;

public class MeasuresSpinnerAdapter extends ArrayAdapter<ConversionFactorsRecord> {

        // Your sent context
        private Context context;

        // Your custom values for the spinner (User)
        private ConversionFactorsRecord[] values;

        public MeasuresSpinnerAdapter(Context context, ConversionFactorsRecord[] values) {
            super(context, 0, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.length;
        }

        @Override
        public ConversionFactorsRecord getItem(int position){
            return values[position];
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            ConversionFactorsRecord currentItem = getItem(position);

            if (currentItem != null) {
                textView.setText(currentItem.getName());
            }

            return convertView;
        }

        // And here is when the "chooser" is popped up
        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
            }

            TextView textView = convertView.findViewById(android.R.id.text1);
            ConversionFactorsRecord currentItem = getItem(position);

            if (currentItem != null) {
                textView.setText(currentItem.getName());
            }

            return convertView;
        }



}
