package com.angleseahospital.admin.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angleseahospital.admin.R;
import com.angleseahospital.admin.firestore.Constants;
import com.angleseahospital.admin.firestore.NurseAdapter;
import com.angleseahospital.admin.firestore.NurseRoster;
import com.angleseahospital.admin.firestore.Shift;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EntireRosterDayView extends LinearLayout {

    private Calendar day;

    private Context context;
    private View view;

    Map<Shift.ShiftType, RecyclerView> recyclers = new HashMap<>();
    Map<Shift.ShiftType, NurseAdapter> adapters = new HashMap<>();

    public EntireRosterDayView(Context context) {
        this(context, null);
    }

    public EntireRosterDayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EntireRosterDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.entire_roster_day_view, this);

        recyclers.put(Shift.ShiftType.AM, view.findViewById(R.id.rv_roster_day_am));
        recyclers.put(Shift.ShiftType.PM, view.findViewById(R.id.rv_roster_day_pm));
        recyclers.put(Shift.ShiftType.NIGHT, view.findViewById(R.id.rv_roster_day_night));

        adapters.put(Shift.ShiftType.AM, new NurseAdapter(false));
        adapters.put(Shift.ShiftType.PM, new NurseAdapter(false));
        adapters.put(Shift.ShiftType.NIGHT, new NurseAdapter(false));

        RecyclerView rv;
        for (Shift.ShiftType key : recyclers.keySet()) {
            rv = recyclers.get(key);
            rv.setAdapter(adapters.get(key));
            rv.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public Map<Shift.ShiftType, NurseAdapter> getAdapters() {
        return adapters;
    }

    public void displayDay(@NonNull Calendar day) {
        this.day = day;

        clear();
        FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTION_ROSTERS + "/" + NurseRoster.getWeeksDate(day) + "/nurses")
                .orderBy(toString(day), Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        //TODO: Show error message
                        Log.d("ENTIRE ROSTER DAY VIEW:", "Failed to query collection: " + task.getException());
                        return;
                    }

                    QuerySnapshot query = task.getResult();
                    if (query == null) {
                        Log.d("Roster Day View", "displayDay: Query was null");
                        return;
                    }

                    Shift.ShiftType type;
                    for (QueryDocumentSnapshot doc : query) {
                        type = Shift.ShiftType.fromString((String) doc.get(toString(day)));
                        if (type != Shift.ShiftType.NONE) {
                            adapters.get(type).addNurse(doc.getId());
                            Log.d("ROSTER DAY VIEW", "displayDay: Nurse added");
                        } else {
                            Log.d("ROSTER DAY VIEW", "displayDay: Nurse had shift type of NONE");
                        }
                    }
                });
    }

    public void clear() {
        NurseAdapter adapter;
        for (Shift.ShiftType key : adapters.keySet()) {
            adapter = adapters.get(key);
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private String toString(Calendar day) {
        return Shift.ShiftDay.values()[day.get(Calendar.DAY_OF_WEEK) - 1].name().toLowerCase();
    }

    public Calendar getDay() {
        return day;
    }
}
