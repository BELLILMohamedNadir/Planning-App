package com.example.planning.recyclerviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planning.R;
import com.example.planning.interfaces.OnClick;
import com.example.planning.models.Note;
import com.example.planning.utils.DateUtils;

import java.util.List;

public class DateRecyclerView extends RecyclerView.Adapter<DateRecyclerView.DateViewHolder> {

    private List<String> data;
    private OnClick listener;
    private int selectedPosition = -1;
    private Context context;

    public DateRecyclerView(Context context, List<String> data, OnClick listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.card_date,null,false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        holder.onBind(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        TextView txt_date;
        RelativeLayout layout;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_date=itemView.findViewById(R.id.txt_date);
            layout=itemView.findViewById(R.id.layout_date);
            itemView.setOnClickListener(view -> {
                listener.onClick(data.get(getAdapterPosition()));
                selectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
        void onBind(String date, int position){
            txt_date.setText(date);
            if (selectedPosition == position) {
                layout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
                txt_date.setTextColor(context.getResources().getColor(android.R.color.white));
            } else {
                layout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                txt_date.setTextColor(context.getResources().getColor(android.R.color.black));
            }
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
