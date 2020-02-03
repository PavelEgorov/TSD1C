package com.egorovsoft.tsd1c.RecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egorovsoft.tsd1c.R;
import com.egorovsoft.tsd1c.data.ScanItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<ScanItem> data;

    public ItemAdapter(ArrayList<ScanItem> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_of_scancode, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getName().setText(data.get(position).getName());
        holder.getCount().setText(String.format("%d", data.get(position).getCount()));
        holder.getScancode().setText(data.get(position).getScanCode());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_item_name;
        private TextView tv_item_scancode;
        private TextView tv_item_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_item_count = itemView.findViewById(R.id.tv_item_count);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_scancode = itemView.findViewById(R.id.tv_item_scancode);
        }

        public TextView getName() {
            return tv_item_name;
        }

        public void setName(TextView tv_item_name) {
            this.tv_item_name = tv_item_name;
        }

        public TextView getScancode() {
            return tv_item_scancode;
        }

        public void setScancode(TextView tv_item_scancode) {
            this.tv_item_scancode = tv_item_scancode;
        }

        public TextView getCount() {
            return tv_item_count;
        }

        public void setCount(TextView tv_item_count) {
            this.tv_item_count = tv_item_count;
        }
    }
}
