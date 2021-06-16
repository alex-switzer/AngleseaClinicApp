package com.angleseahospital.admin.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
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

    /**
     * Once fragment is given a view inflate it's layout
     * @param inflater Layout inflater for this fragment
     * @param container Parent container for this fragment
     * @param savedInstanceState Bundle for previous fragment instance
     * @return View for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return v = inflater.inflate(R.layout.frag_viewentireroster,container,false);
    }

    /**
     * Sets up this fragment once layout is inflated
     * @param savedInstanceState Bundle from prior instance
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Once the activity is created. Assign View members with their respective views
        calendarContainer = v.findViewById(R.id.roster_calendar_container);
        calendar = v.findViewById(R.id.roster_calendar_select);
        rosterDayView = v.findViewById(R.id.roster_day_view);

        //Setup listener for the CalendarView which displays the roster for chosen day
        calendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            Log.d("CALENDAR CHANGED", year + "/" + (month + 1 < 10 ? "0" + (month + 1) : month + 1) + "/" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));

            Calendar day = Calendar.getInstance();
            day.setFirstDayOfWeek(Calendar.MONDAY);
            day.set(Calendar.YEAR, year);
            day.set(Calendar.MONTH, month);
            day.set(Calendar.DATE, dayOfMonth);
            Log.d("CALENDAR CHANGED", "Date:" + day.get(Calendar.DATE));

            openRoster(day);
            closeCalendar();

        });

        //Attach OnItemClickListeners to all RosterView adapters
        rosterDayView.getAdapters().forEach(
            (shiftType, adapter) ->
                adapter.setOnItemClickListener(position ->
                    //Open AddEditNurse fragment with selected nurse
                    MainActivity.changeCurrentFragment(getHost(), getParentFragmentManager(), new AddEditNurse(adapter.get(position)), R.id.nav_addNurse)
        ));

        //Display today's roster
        rosterDayView.displayDay(Calendar.getInstance());
        //Ensure the CalendarView is closed
        closeCalendar();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Add the view_entire_roster_menu to the current toolbar / options menu
        inflater.inflate(R.menu.view_entire_roster_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Activates option menus buttons functionality respectively
        if (item.getItemId() == R.id.menu_openCalendar) {
            if (calendarOpen)
                closeCalendar();
            else
                openCalendar();
            //Consumes item selected call
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays the roster for the given day
     * @param day Day to display the roster for
     */
    private void openRoster(Calendar day) {
        rosterDayView.displayDay(day);
    }

    /**
     * Displays and overlaps the CalendarView
     */
    private void openCalendar() {
        calendarOpen = true;
        calendarContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Removes the CalendarView overlay
     */
    private void closeCalendar() {
        calendarOpen = false;
        calendarContainer.setVisibility(View.GONE);
    }

}