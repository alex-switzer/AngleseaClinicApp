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

    /**
     * Add a click listener for RecyclerView items
     * @param clickListener Listener triggered once an item is clicked
     */
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * View holder for every Nurse item
     */
    public static class NurseViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_firstname;
        public TextView txt_lastname;
        public ImageView img_edit;

        public View itemView;

        /**
         * The view holder for each item in the RecyclerView
         * @param itemView View for the specific item
         * @param clickListener Click Listener for the specific item
         */
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

    /**
     * Constructs a NurseAdapter
     * @param fill Fill the adapter with nurses from the database
     */
    public NurseAdapter(boolean fill) {
        if (fill)
            fill();
    }

    /**
     * Constructs a NurseAdapter
     * @param nurses List of Nurse objects to be inserted to the adapter once constructed
     */
    public NurseAdapter(@NonNull ArrayList<Nurse> nurses) {
        this.nurses = nurses;
    }

    /**
     * Clears then fills the Adapter with nurses from the database
     */
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

    /**
     * Constructs a Nurse object from a nurse in the database with given ID, then gets added to the Adapter
     * @param nurseID Nurse ID to be added to Adapter
     */
    public void addNurse(String nurseID) {
        FirebaseFirestore.getInstance()
                .document(Constants.COLLECTION_NURSES + "/" + nurseID)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        return;

                    DocumentSnapshot nurseDoc = task.getResult();
                    if (nurseDoc == null)
                        return;
                    addNurse(new Nurse(nurseDoc));
                });
    }

    /**
     * Adds a Nurse object to the Adapter
     * @param nurse
     */
    public void addNurse(Nurse nurse) {
        nurses.add(nurse);
        notifyItemInserted(nurses.size() - 1);
        //TODO: Sort NurseAdapter by ID?
    }

    /**
     * Gets the Nurse object at given position
     * @param position Position of nurse to retrieve
     * @return Returns the Nurse object from given position. Null if adapter has not been constructed before or position is out of bounds
     */
    public Nurse get(int position) {
        if (nurses == null)
            return null;
        if (position < 0 || nurses.size() - 1 >= position)
            return nurses.get(position);
        return null;
    }

    /**
     * Removes all Nurse objects from the Adapter
     */
    public void clear() {
        if (nurses != null) {
            nurses.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * Inflates the Adapters layout and returns the NurseViewHolder constructed
     * @param parent The parent view group
     * @param viewType The type of view
     * @return Returns a NurseViewHolder constructed from the inflated layout
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nurse_item, parent, false);
        return new NurseViewHolder(v, clickListener);
    }

    /**
     * Sets up a item display details once view holder is bound
     * @param holder The ViewHolder for the Adapter
     * @param position Items position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Nurse nurse = nurses.get(position);
        ((NurseViewHolder) holder).txt_firstname.setText(nurse.firstName);
        ((NurseViewHolder) holder).txt_lastname.setText(nurse.lastName);
    }

    /**
     * Gets the total amount of Nurse objects in the Adapter
     * @return Returns the total amount of nurses in the Adapter. Returns -1 if previously not constructed
     */
    @Override
    public int getItemCount() {
        if (nurses == null)
            return -1;
        return nurses.size();
    }
}
