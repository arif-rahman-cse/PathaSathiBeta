package com.example.pathasathi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pathasathi.R;
import com.example.pathasathi.model.AvailablePothSathi;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AvailableSatheAdapter extends RecyclerView.Adapter<AvailableSatheAdapter.MyHolder> {

    private Context context;
    private List<AvailablePothSathi> availablePothSathiList;

    public AvailableSatheAdapter(Context context, List<AvailablePothSathi> availablePothSathiList) {
        this.context = context;
        this.availablePothSathiList = availablePothSathiList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.available_ps_item_view, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        AvailablePothSathi sathi = availablePothSathiList.get(i);
        myHolder.circleImageView.setImageDrawable(context.getResources().getDrawable(sathi.getImage()));
        myHolder.nameText.setText(sathi.getName());

        myHolder.distanceText.setText("Distance : " + sathi.getDistance() + " km");


    }

    @Override
    public int getItemCount() {
        return availablePothSathiList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView nameText, distanceText;
        Button callForHelpButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            nameText = itemView.findViewById(R.id.nameTextId);
            distanceText = itemView.findViewById(R.id.distanceId);

        }
    }
}
