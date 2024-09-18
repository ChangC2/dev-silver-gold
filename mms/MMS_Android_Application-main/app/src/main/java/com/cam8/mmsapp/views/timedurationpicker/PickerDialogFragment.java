package com.cam8.mmsapp.views.timedurationpicker;

public class PickerDialogFragment extends TimeDurationPickerDialogFragment {

    public interface TimeDurationListener {
        public void onDurationSet(int tag, long duration);
    }

    long duration = 15 * 60 * 1000;
    int tag = 0;
    TimeDurationListener timeDurationListener;

    public PickerDialogFragment(long duration, int tag, TimeDurationListener timeDurationListener) {
        super();
        this.duration = duration;
        this.tag = tag;
        this.timeDurationListener = timeDurationListener;
    }

    @Override
    protected long getInitialDuration() {
        return duration;
    }

    @Override
    protected int setTimeUnits() {
        return TimeDurationPicker.HH_MM_SS;
    }

    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
        if (timeDurationListener != null) {
            timeDurationListener.onDurationSet(tag, duration);
        }
    }
}
