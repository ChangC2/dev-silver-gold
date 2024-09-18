
package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * This IValueFormatter is just for convenience and simply puts a "%" sign after
 * each value. (Recommeded for PieChart)
 *
 * @author Philipp Jahoda
 */
public class PercentFormatter implements IValueFormatter, IAxisValueFormatter
{

    protected DecimalFormat mFormatWithPoint;
    protected DecimalFormat mFormatWithoutPoint;

    public PercentFormatter() {

        mFormatWithPoint = new DecimalFormat("###,###,##0.0");
        mFormatWithoutPoint = new DecimalFormat("###,###,##0");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format1
     * @param format2
     */
    public PercentFormatter(DecimalFormat format1, DecimalFormat format2) {
        this.mFormatWithPoint = format1;
        this.mFormatWithoutPoint = format2;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // return mFormatWithPoint.format(value) + " %";

        int valueInt = (int) value;
        if (value - valueInt == 0) {
            return mFormatWithoutPoint.format(value) + " %";
        } else {
            return mFormatWithPoint.format(value) + " %";
        }
    }

    // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        //return mFormatWithPoint.format(value) + " %";

        int valueInt = (int) value;
        if (value - valueInt == 0) {
            return mFormatWithoutPoint.format(value) + " %";
        } else {
            return mFormatWithPoint.format(value) + " %";
        }
    }

    public int getDecimalDigits() {
        return 1;
    }
}
