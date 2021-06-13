package com.angleseahospital.admin.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;

import com.angleseahospital.admin.MainActivity;
import com.angleseahospital.admin.R;
import com.angleseahospital.admin.classes.EntireRosterDayView;
import com.angleseahospital.admin.firestore.NurseAdapter;

import java.util.Calendar;

public class ViewEntireRoster extends Fragment {

    private View v;
    private FrameLayout calendarContainer;
    private CalendarView calendar;
    private EntireRosterDayView rosterDayView;

    private boolean calendarOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return v = inflater.inflate(R.layout.frag_viewentireroster,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        calendarContainer = v.findViewById(R.id.roster_calendar_container);
        calendar = v.findViewById(R.id.roster_calendar_select);
        rosterDayView = v.findViewById(R.id.roster_day_view);

        calendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            openRoster(year, month, dayOfMonth);
            closeCalendar();
        });

        rosterDayView.getAdapters().forEach(
            (shiftType, adapter) ->
                adapter.setOnItemClickListener(position ->
                    MainActivity.changeCurrentFragment(getHost(), getParentFragmentManager(), new AddEditNurse(adapter.get(position)), R.id.nav_addNurse)
        ));

        rosterDayView.displayDay(Calendar.getInstance());
        closeCalendar();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_entire_roster_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_openCalendar) {
            if (calendarOpen)
                closeCalendar();
            else
                openCalendar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openRoster(int year, int month, int dayOfMonth) {
        Calendar day = Calendar.getInstance();
        day.setFirstDayOfWeek(Calendar.MONDAY);
        day.set(year, month, dayOfMonth);
        rosterDayView.displayDay(day);
    }

    private void openCalendar() {
        calendarOpen = true;
        calendarContainer.setVisibility(View.VISIBLE);
    }

    private void closeCalendar() {
        calendarOpen = false;
        calendarContainer.setVisibility(View.GONE);
    }

}