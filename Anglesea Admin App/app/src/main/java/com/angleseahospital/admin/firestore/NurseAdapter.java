package com.angleseahospital.admin.firestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angleseahospital.admin.R;

import java.util.ArrayList;

public class NurseAdapter extends RecyclerView.Adapter {

    private ArrayList<Nurse> nurses;

    public static class NurseViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_fullname;
        public ImageView img_edit;

        public NurseViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_fullname = itemView.findViewById(R.id.nurseItem_txt_name);
            img_edit = itemView.findViewById(R.id.nurseItem_img_edit);
        }
    }

    public NurseAdapter() {
        //TODO: Load nurses from Firestore
    }

    public NurseAdapter(ArrayList<Nurse> nurses) {
        this.nurses = nurses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nurse, parent, false);
        return new NurseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Nurse nurse = nurses.get(position);
        ((NurseViewHolder) holder).txt_fullname.setText(nurse.getFullName());
    }

    @Override
    public int getItemCount() {
        return nurses.size();
    }

}
