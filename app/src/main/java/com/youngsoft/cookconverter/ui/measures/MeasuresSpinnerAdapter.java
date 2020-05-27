package com.youngsoft.cookconverter.ui.measures;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;

import java.util.ArrayList;
import java.util.List;

public class MeasuresSpinnerAdapter extends ArrayAdapter<ConversionFactorsRecord> {

        //TODO: https://codinginflow.com/tutorials/android/custom-spinner
        //TODO: https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list

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


        // And the "magic" goes here
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

            /**
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(values[position].getName());

            // And finally return your dynamic (or custom) view for each spinner item
            return label;
            **/

        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
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
            /**
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].getName());

            return label;
             **/
        }

}
