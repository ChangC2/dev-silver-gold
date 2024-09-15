package GLG;

import java.util.ArrayList;

import view.MainView;
/////////////////////////////////////////////////////////////////////// 
// Application should provide custom implemnetation of LiveDataFeed.
/////////////////////////////////////////////////////////////////////// 

public class LiveDataFeed implements DataFeedInterface {
    GlgChart glg_chart;
    static boolean IsInitialized = false;
    double valueTarget;
    double valueProgNum;
    double valueLeadInFR;
    double valueIdle;
    double valueHighLimit;
    double valueLowLimitTime;
    double valueWearLimit;
    MainView mainView = MainView.getInstance();

    ///////////////////////////////////////////////////////////////////////
    // Constructor.
    ///////////////////////////////////////////////////////////////////////
    LiveDataFeed(GlgChart chart) {
        glg_chart = chart;

//      valueTarget = mainView.getValueTarget();
//      valueLeadInFR = mainView.getValueLeadInFR();
//      valueIdle = mainView.getValueIdle();
//      valueHighLimit = mainView.getValueHighLimit();
//      valueLowLimit = mainView.getValueLowLimit();
//      valueWearLimit = mainView.getValueWearLimit();

        // Initialize data feed. Generate an error on failure and quit.
        if (!Initialize())
            glg_chart.error("DataFeed initialization failed.", true);
    }

    ///////////////////////////////////////////////////////////////////////
    // Initiaze DataFeed.
    ///////////////////////////////////////////////////////////////////////
    public boolean Initialize() {
        if (IsInitialized)
            return true;    // Data feed has been already initialized.

        /* Place custom code here to initialize data feed.
             Return false on failure.
        */

        valueProgNum = mainView.getValueProgNum();
        if (valueProgNum == 1) {
            LiveDataFeed.IsInitialized = false;
        }

        return true;
    }

    ///////////////////////////////////////////////////////////////////////
    // Query data for an individual data sample for a given plot_index.
    ///////////////////////////////////////////////////////////////////////
    public boolean GetPlotPoint(int plot_index, DataPoint data_point) {
      /* Provide custom code to fill in data_point fields. 
         If failed, return false.
      */
        boolean isTeachMode = mainView.isTeachMode();
        switch (plot_index) {
            case 0:     // Target
                data_point.value = mainView.getValueTarget();
                data_point.value_valid = true;
                break;
            case 1:     // High
                if (mainView.isAdaptiveOn()) {
                    data_point.value = mainView.getValueAdaptiveHighPercentage();

                    // When teachmode=1, then don't show plot
                    if (mainView.isTeachMode()) {
                        data_point.value_valid = false;
                    } else {
                        data_point.value_valid = true;
                    }
                } else {
                    data_point.value = mainView.getValueHighLimit();
                    data_point.value_valid = true;
                }
                // System.out.println(data_point.value);
                break;
            case 2:     // Wear
                if (mainView.isAdaptiveOn()) {
                    data_point.value = mainView.getValueAdaptiveWearPercentage();

                    // When teachmode=1, then don't show plot
                    if (mainView.isTeachMode()) {
                        data_point.value_valid = false;
                    } else {
                        data_point.value_valid = true;
                    }
                } else {
                    data_point.value = mainView.getValueWearLimit();
                    data_point.value_valid = true;
                }
                break;
            case 3:     // Low
                // *Currently We don't use this plot
                data_point.value = mainView.getValueLowLimitTimer();
                data_point.value_valid = false;
                break;
            case 4:     // HP
                data_point.value = mainView.getValueHP();
                data_point.value_valid = true;
                break;
            case 5:     // Lead In Trigger
                data_point.value = mainView.getValueLeadInTrigger();
                if (!isTeachMode && mainView.isLeadInFROn()) {
                    data_point.value_valid = true;
                } else {
                    data_point.value_valid = false;
                }
                break;
            case 6:     // Idle
                data_point.value = mainView.getValueIdle();
                data_point.value_valid = true;
                //System.out.println(data_point.value + " IDLE");
                break;
            case 7:     // FeedrateScale
                data_point.value = mainView.getValueFeedrateScale();
                if (!isTeachMode && mainView.isAdaptiveOn()) {
                    //System.out.println(data_point.value + " LEAD IN FR");
                    data_point.value_valid = true;
                } else {
                    data_point.value_valid = false;
                }
                break;
            case 8:     // Current Sequence Number
                data_point.value = mainView.getCurrentSequenceNumber();
                data_point.value_valid = true;

                break;
            case 9:     // Current Sequence Number
                data_point.value = mainView.getHP2();
                data_point.value_valid = true;

                break;
            default:
                data_point.value = 0;
                data_point.value_valid = false;
                break;
        }

        //data_point.value_valid = true;

        if (glg_chart.SUPPLY_TIME_STAMP)
            // Place custom code here to supply time stamp.
            data_point.time_stamp = glg_chart.GetCurrTime();
        else
            // Chart will automatically supply time stamp using current time.
            data_point.time_stamp = 0.;

        return true;
    }

    ///////////////////////////////////////////////////////////////////////
    // Query historical chart data for a provided time interval for a given
    // plot index.
    ///////////////////////////////////////////////////////////////////////
    public ArrayList<DataPoint>
    GetHistPlotData(int plot_index, double start_time, double end_time,
                    int max_num_samples) {
        if (max_num_samples < 1)
            max_num_samples = 1;
        int num_samples = max_num_samples;

        ArrayList<DataPoint> data_array = new ArrayList<DataPoint>();
        for (int i = 0; i < num_samples; ++i) {
            DataPoint data_point = new DataPoint();

         /* Provide custom code to fill in data_point fields.

         data_point.value = 
         data_point.time_stamp = 
         data_point.value_valid = true;
         */

            data_array.add(data_point);
            //System.out.println(data_point.value);
        }

        return data_array;
    }
}

