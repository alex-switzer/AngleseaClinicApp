package com.angleseahospital.admin.firestore;

import java.time.LocalDate;
import java.util.Calendar;

public class Shift {

    /**
     * Possible days for shifts
     */
    public enum ShiftDay {
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        SUN;

        /**
         * Gets whether today is equal to this ShiftDay
         * @return Whether today is equal to this ShiftDay
         */
        public boolean isToday() {
            return LocalDate.now().getDayOfWeek().ordinal() == ordinal();
        }
    }

    /**
     * Types of possible shifts
     */
    public enum ShiftType {
        /**
         * Null version for Shift Types
         */
        NONE (null, null, false),

        /**
         * An AM Shift
         */
        AM (ShiftTime.AM_START, ShiftTime.AM_END, false),

        /**
         * A PM Shift
         */
        PM (ShiftTime.PM_START, ShiftTime.PM_END, false),

        /**
         * A Night Shift
         */
        NIGHT(ShiftTime.NIGHT_START, ShiftTime.NIGHT_END, true);

        /**
         * Shift types start time
         */
        public ShiftTime startTime;

        /**
         * Shift types end time
         */
        public ShiftTime endTime;

        /**
         * Whether this shift is overnight
         */
        public boolean overnight;

        /**
         * Whether this shift has been completed
         */
        private boolean isCompleted;

        /**
         * Constructs a ShiftType
         * @param start Start time for this ShiftType
         * @param endTime End time for this ShiftType
         * @param overnight Whether this ShiftType is overnight
         */
        ShiftType(ShiftTime start, ShiftTime endTime, boolean overnight) {
            this.startTime = start;
            this.endTime = endTime;
            this.overnight = overnight;
            this.isCompleted = false;
        }

        /**
         * Returns a ShiftType that equals the given string
         * @param string String to be queried
         * @return ShiftType from given String.
         *         ShiftType.{@code ShiftType.NONE} if the string could be evaluated
         */
        public static ShiftType fromString(String string) {
            if (string == null)
                return null;

            for (int i = 0; i < values().length; i++)
                if (values()[i].name().equals(string))
                    return ShiftType.values()[i];
            return ShiftType.NONE;
        }

        /**
         * Whether the Shift has been completed
         * @return Whether the Shift has been completed
         */
        public boolean isCompleted() { return isCompleted; }

        /**
         * Completed the current ShiftType
         */
        public void complete() { isCompleted = true; }
    }

    /**
     * Possible ShiftTimes for each shift
     */
    public enum ShiftTime {
        AM_START ("07:00"),
        AM_END ("15:30"),
        PM_START ("14:00"),
        PM_END ("22:30"),
        NIGHT_START ("22:00"),
        NIGHT_END ("07:30");

        /**
         * Constructs the all ShiftTimes
         * @param time Time set for each ShiftTime
         */
        ShiftTime(String time) { this.time = time; }

        public String time;
    }

    /**
     * The day of the Shift
     */
    public ShiftDay day;

    /**
     * The type of the Shift
     */
    public ShiftType type;

    /**
     * Constructs the Shift with given day and type
     * @param day
     * @param type
     */
    public Shift(ShiftDay day, ShiftType type) {
        this.day = day;
        this.type = type;
    }

    /**
     * Creates a formatted string for the current time
     * @return Formatted string for today. i.e hh:mm
     */
    public static String get24Time() {
        Calendar cal = Calendar.getInstance();

        String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
        hour = hour.length() == 1 ? "0" + hour : hour;

        String min = cal.get(Calendar.MINUTE) + "";
        min = min.length() == 1 ? "0" + min : min;
        return hour + ":" + min;
    }
}
