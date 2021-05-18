package com.angleseaadminapp.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angleseaadminapp.R;

import java.util.ArrayList;

public class NurseAdapter extends RecyclerView.Adapter<NurseAdapter.NurseViewHolder> {

    ArrayList<Nurse> nurses;

    public NurseAdapter() {}
    
    public NurseAdapter(ArrayList<Nurse> nuses) {
        this.nurses = nuses;
    }


    @NonNull
    @Override
    public NurseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(c).inflate(R.layout.nurse_item ,parent,false); //TODO: make nurse item
        //return new NurseViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NurseAdapter.NurseViewHolder holder, int position) {
        final Nurse s = nurses.get(position);

        //TODO: Holder write to textviews

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return nurses.size();
    }

    public class NurseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //TODO: make right text views
        //TextView nameTxt, propTxt, descTxt;
        ItemClickListener itemClickListener;

        public NurseViewHolder(View itemView) {
            super(itemView);

            //TODO: find text views
            //nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            //propTxt = (TextView) itemView.findViewById(R.id.propellantTxt);
            //descTxt = (TextView) itemView.findViewById(R.id.descTxt);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }
}
