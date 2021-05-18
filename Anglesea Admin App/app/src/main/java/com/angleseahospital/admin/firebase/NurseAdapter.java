package com.angleseahospital.admin.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angleseahospital.admin.R;

import java.util.ArrayList;

public class NurseAdapter extends RecyclerView.Adapter<NurseAdapter.NurseViewHolder> {

    Context c;
    ArrayList<Nurse> nurses;

    public NurseAdapter(Context c) {
        this.c = c;
    }

    public NurseAdapter(Context c, ArrayList<Nurse> nurses) {
        this.c = c;
        this.nurses = nurses;
    }

    @NonNull
    @Override
    public NurseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_nurse ,parent,false);

        return new NurseViewHolder(v);
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
        TextView nameTxt;
        ItemClickListener itemClickListener;

        public NurseViewHolder(View itemView) {
            super(itemView);

            //TODO: find text views
            nameTxt = (TextView) itemView.findViewById(R.id.text_view_nurse_name);

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
