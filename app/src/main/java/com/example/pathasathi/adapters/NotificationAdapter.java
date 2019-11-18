package com.example.pathasathi.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.pathasathi.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private String[] notificationList;

    public NotificationAdapter( Context context, String[] notificationList){
        this.context = context;
        this.notificationList = notificationList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context).inflate(R.layout.notification_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.textView.setText(notificationList[i]);
        if (i%3==0){
            viewHolder.linearLayout.setBackgroundResource(R.color.light_black);
        }

    }


    @Override
    public int getItemCount() {
        return notificationList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.notification_item);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}
