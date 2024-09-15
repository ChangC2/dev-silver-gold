package Model;

public class ShiftTime {
    // Shift Time and Shift End
    // 01:00 ~ 05:30 => 1 ~ 5.5
    float start;
    float end;

    public ShiftTime(String startTime, String endTime) {
        start = convertStringToFloat(startTime);
        end = convertStringToFloat(endTime);

        //Log.e("ShiftTime", toString());
    }

    private float convertStringToFloat(String timeString) {
        float result = 0;
        timeString = timeString.trim();

        // Try converting to float first
        try {
            result = new Float(timeString);
        } catch (NumberFormatException nfe) {
            // OK so that didn't work.  Did they use a colon?
            if (timeString.contains(":")) {
                int hours = 0;
                int minutes = 0;
                int locationOfColon = timeString.indexOf(":");
                try {
                    hours = new Integer(timeString.substring(0, locationOfColon));
                    minutes = new Integer(timeString.substring(locationOfColon + 1));
                } catch (NumberFormatException nfe2) {
                    //need to do something here if they are still formatted wrong.
                    //perhaps throw the exception to the user to the UI to force the user
                    //to put in a correct value.
                    throw nfe2;
                }

                // Float Hours mode
                /*
                //add in partial hours (ie minutes if minutes are greater than zero.
                if (minutes > 0) {
                    result = minutes / 60.0f;
                }

                // now add in the full number of hours.
                result += hours;
                */

                // Float Minutes mode
                result = hours * 60 + minutes;
            }
        }

        return result;
    }

    public float getStart() { return start; }
    public float getEnd() { return end; }

    // Check time is in the current shift
    public boolean isTimeInTheShift(String timeString) {
        float time = convertStringToFloat(timeString);

        if (end > start ) {
            if (time >= start && time < end) {
                return true;
            }
        } else {
            if (time >= start && time > end) {
                return true;
            } else if (time < start && time < end) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShiftTime shiftTime = (ShiftTime) o;

        if (start != shiftTime.start) return false;
        return end == shiftTime.end;
    }

    @Override
    public String toString() {

        // Float Hours mode
        /*int sHours = (int) start;
        int sMinutes = (int) ((start - sHours) * 60);

        int eHours = (int) end;
        int eMinutes = (int) ((end - eHours) * 60);*/

        // Float Minutes mode
        int sHours = (int) start / 60;
        int sMinutes = (int) start % 60;

        int eHours = (int) end / 60;
        int eMinutes = (int) end % 60;

        return String.format("%02d:%02d-%02d:%02d", sHours, sMinutes, eHours, eMinutes);
    }
}
