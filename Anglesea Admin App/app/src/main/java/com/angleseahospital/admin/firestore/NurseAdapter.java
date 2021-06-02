package com.angleseahospital.admin.firestore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angleseahospital.admin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NurseAdapter extends RecyclerView.Adapter {

    private ArrayList<Nurse> nurses;

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class NurseViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_fullname;
        public ImageView img_edit;

        public NurseViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener == null)
                        return;
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION);
                        clickListener.onItemClick(position);
                }
            });
            txt_fullname = itemView.findViewById(R.id.nurseItem_txt_name);
            img_edit = itemView.findViewById(R.id.nurseItem_img_edit);
        }
    }

    public NurseAdapter() {
        Log.d("DEBUGGING NURSE ADAPTER", "NurseAdapter created");
        FirebaseFirestore.getInstance().collection("nurses").get().continueWith(new Continuation<QuerySnapshot, Object>() {
            @Override
            public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                if (!task.isSuccessful())
                    return null;

                QuerySnapshot query;
                if ((query = task.getResult()) == null)
                    return null;

                nurses = new ArrayList<>();
                for (QueryDocumentSnapshot doc : query)
                    nurses.add(new Nurse(doc));
                notifyDataSetChanged();
                return null;
            }
        });
    }

    public NurseAdapter(ArrayList<Nurse> nurses) {
        this.nurses = nurses;
    }

    public void addNurse(Nurse nurse) {
        nurses.add(nurse);
        //TODO: Sort by ID?
    }

    public Nurse get(int position) {
        if (nurses == null)
            return null;
        return nurses.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nurse, parent, false);
        return new NurseViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Nurse nurse = nurses.get(position);
        ((NurseViewHolder) holder).txt_fullname.setText(nurse.getFullName());
    }

    @Override
    public int getItemCount() {
        if (nurses == null)
            return 0;
        return nurses.size();
    }
}
