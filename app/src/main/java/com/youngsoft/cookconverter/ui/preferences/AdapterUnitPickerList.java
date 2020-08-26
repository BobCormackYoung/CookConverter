package com.youngsoft.cookconverter.ui.preferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;

public class AdapterUnitPickerList extends ListAdapter<ConversionFactorsRecord, AdapterUnitPickerList.ViewHolder> {

    private OnUnitClickListener onUnitClickListener;

    private static final DiffUtil.ItemCallback<ConversionFactorsRecord> DIFF_CALLBACK = new DiffUtil.ItemCallback<ConversionFactorsRecord>() {
        @Override
        public boolean areItemsTheSame(@NonNull ConversionFactorsRecord oldItem, @NonNull ConversionFactorsRecord newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ConversionFactorsRecord oldItem, @NonNull ConversionFactorsRecord newItem) {
            return false;
        }
    };


    public AdapterUnitPickerList(OnUnitClickListener onUnitClickListener) {
        super(DIFF_CALLBACK);
        this.onUnitClickListener = onUnitClickListener;
    }

    @NonNull
    @Override
    public AdapterUnitPickerList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_preferences, parent, false);
        return new AdapterUnitPickerList.ViewHolder(view, onUnitClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUnitPickerList.ViewHolder holder, int position) {
        ConversionFactorsRecord conversionFactorsRecord = getItem(position);

        holder.textViewName.setText(conversionFactorsRecord.getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        OnUnitClickListener onUnitClickListener;

        public ViewHolder(@NonNull View itemView, OnUnitClickListener onUnitClickListener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_unit_picker_name);
            this.onUnitClickListener = onUnitClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onUnitClickListener.onUnitClick(getItem(getAdapterPosition()));
        }
    }

    public interface OnUnitClickListener {
        void onUnitClick(ConversionFactorsRecord index);
    }

}
