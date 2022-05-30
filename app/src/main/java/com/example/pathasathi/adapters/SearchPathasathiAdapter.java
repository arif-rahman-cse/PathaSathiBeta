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
import com.example.pathasathi.model.SearchPathasathi;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchPathasathiAdapter extends RecyclerView.Adapter<SearchPathasathiAdapter.MyViewHolder> {

    private static final String TAG = "OnlineDoctorsAdapter";
    private Context context;
    private ArrayList<SearchPathasathi> searchPathasathisArrayList;
    private RecyclerViewItemClickListener callBack;

    public SearchPathasathiAdapter(Context context, ArrayList<SearchPathasathi> searchPathasathisArrayList, RecyclerViewItemClickListener callBack) {
        this.context = context;
        this.searchPathasathisArrayList = searchPathasathisArrayList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_pathasathi_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPathasathiAdapter.MyViewHolder holder, final int position) {

        if (searchPathasathisArrayList != null) {
            holder.name.setText(searchPathasathisArrayList.get(position).getName());
            //holder.about.setText(searchPathasathisArrayList.get(position).getSpecialty());
            //holder.degree.setText(searchPathasathisArrayList.get(position).getDegree());

            holder.addPathasathi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Add Pathasathi");
                    callBack.didPressed(searchPathasathisArrayList.get(position).getUser_id());
                }
            });
        } else {
            Log.d(TAG, "onBindViewHolder: No Doctors Available ");
        }


    }

    @Override
    public int getItemCount() {
        return searchPathasathisArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView about;
        CircleImageView avator;
        ImageView addPathasathi;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_tv);
            about = itemView.findViewById(R.id.about_you_tv);
            avator = itemView.findViewById(R.id.avator_iv);
            addPathasathi = itemView.findViewById(R.id.add_pathasathi_iv);

        }


    }
}
