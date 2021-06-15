package com.angleseahospital.admin.firestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angleseahospital.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NurseAdapter extends RecyclerView.Adapter {

    private ArrayList<Nurse> nurses = new ArrayList<>();
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class NurseViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_firstname;
        public TextView txt_lastname;
        public ImageView img_edit;

        public View itemView;

        public NurseViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (clickListener == null)
                    return;
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION);
                    clickListener.onItemClick(position);
            });

            this.itemView = itemView;
            txt_firstname = itemView.findViewById(R.id.nurseItem_txt_firstname);
            txt_lastname = itemView.findViewById(R.id.nurseItem_txt_lastname);
            img_edit = itemView.findViewById(R.id.nurseItem_img_edit);
        }
    }

    public NurseAdapter(boolean fill) {
        if (fill)
            fill();
    }

    public void fill() {
        clear();
        FirebaseFirestore.getInstance().collection("nurses").get().continueWith(task -> {
            if (!task.isSuccessful())
                return null;

            QuerySnapshot query;
            if ((query = task.getResult()) == null)
                return null;

            for (QueryDocumentSnapshot doc : query)
                nurses.add(new Nurse(doc));
            notifyDataSetChanged();
            return null;
        });
    }

    public NurseAdapter(ArrayList<Nurse> nurses) {
        this.nurses = nurses;
    }

    public void addNurse(String nurseID) {
        FirebaseFirestore.getInstance()
                .document(Constants.COLLECTION_NURSES + "/" + nurseID)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        return;

                    nurses.add(new Nurse((DocumentSnapshot) task.getResult()));
                    notifyItemInserted(nurses.size() - 1);
                });
    }

    public void addNurse(Nurse nurse) {
        nurses.add(nurse);
        //TODO: Sort NurseAdapter by ID?
    }

    public Nurse get(int position) {
        if (nurses == null)
            return null;
        return nurses.get(position);
    }

    public void clear() {
        if (nurses != null)
            nurses.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nurse_item, parent, false);
        return new NurseViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Nurse nurse = nurses.get(position);
        ((NurseViewHolder) holder).txt_firstname.setText(nurse.firstName);
        ((NurseViewHolder) holder).txt_lastname.setText(nurse.lastName);
    }

    @Override
    public int getItemCount() {
        if (nurses == null)
            return 0;
        return nurses.size();
    }
}
