package com.angleseahospital.nurse.firestore;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Calendar;

public class Shift {

    public enum ShiftDay {
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        SUN;

        public boolean isToday() {
            return LocalDate.now().getDayOfWeek().ordinal() == ordinal();
        }
    }

    public enum ShiftType {
        AM (ShiftTime.AM_START, ShiftTime.AM_END, false),
        PM (ShiftTime.PM_START, ShiftTime.PM_END, false),
        Night (ShiftTime.NIGHT_START, ShiftTime.NIGHT_END, true);

        public ShiftTime startTime;
        public ShiftTime endTime;

        public boolean overnight;
        private boolean isCompleted;

        ShiftType(ShiftTime start, ShiftTime endTime, boolean overnight) {
            this.startTime = start;
            this.endTime = endTime;
            this.overnight = overnight;
            this.isCompleted = false;
        }
        ShiftType(ShiftTime start, ShiftTime endTime, boolean overnight, boolean isCompleted) {
            this.startTime = start;
            this.endTime = endTime;
            this.overnight = overnight;
            this.isCompleted = isCompleted;
        }

        public static ShiftType fromString(String string) {
            if (string == null)
                return null;

            for (int i = 0; i < values().length; i++)
                if (values()[i].name().equals(string))
                    return ShiftType.values()[i];
            return null;
        }

        public boolean isCompleted() { return isCompleted; }
        public void complete() { isCompleted = true; }
    }

    public enum ShiftTime {
        AM_START ("07:00"),
        AM_END ("15:30"),
        PM_START ("14:00"),
        PM_END ("22:30"),
        NIGHT_START ("22:00"),
        NIGHT_END ("07:30");

        ShiftTime(String time) { this.time = time; }

        public String time;

        public double compareTo(String givenTime) {
            String[] strTime = givenTime.split(":");
            String[] strGivenTime = time.split(":");

            int hr = Integer.parseInt(strGivenTime[0]) - Integer.parseInt(strTime[0]);
            int min = Integer.parseInt(strGivenTime[1]) - Integer.parseInt(strTime[1]);

            return ((double)min / 100.0) + hr;
        }
    }

    public ShiftDay day;
    public ShiftType type;

    public Shift(ShiftDay day, ShiftType type) {
        this.day = day;
        this.type = type;
    }

    public static String get24Time() {
        Calendar cal = Calendar.getInstance();

        String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
        hour = hour.length() == 1 ? "0" + hour : hour;

        String min = cal.get(Calendar.MINUTE) + "";
        min = min.length() == 1 ? "0" + min : min;
        return hour + ":" + min;
    }
}
