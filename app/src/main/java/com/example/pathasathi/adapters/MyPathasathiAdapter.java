package com.example.pathasathi.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pathasathi.R;
import com.example.pathasathi.RecyclerViewItemClickListener;
import com.example.pathasathi.model.MyPathasathi;
import com.example.pathasathi.model.SearchPathasathi;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyPathasathiAdapter extends RecyclerView.Adapter<MyPathasathiAdapter.MyViewHolder> {

    private static final String TAG = "OnlineDoctorsAdapter";
    private Context context;
    private ArrayList<MyPathasathi> myPathasathiArrayList;
    private RecyclerViewItemClickListener callBack;

    public MyPathasathiAdapter(Context context, ArrayList<MyPathasathi> myPathasathiArrayList, RecyclerViewItemClickListener callBack) {
        this.context = context;
        this.myPathasathiArrayList = myPathasathiArrayList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_patha_sathi_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPathasathiAdapter.MyViewHolder holder, final int position) {

        if (myPathasathiArrayList != null) {
            holder.name.setText(myPathasathiArrayList.get(position).getName());

            /*
            holder.addPathasathi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Add Pathasathi");
                    callBack.didPressed(myPathasathiArrayList.get(position).getUser_id());
                }
            });

             */

        } else {
            Log.d(TAG, "onBindViewHolder: No Pathasathi  Available ");
        }


    }

    @Override
    public int getItemCount() {
        return myPathasathiArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView avator;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_tv);
            avator = itemView.findViewById(R.id.avator_iv);

        }


    }
}
