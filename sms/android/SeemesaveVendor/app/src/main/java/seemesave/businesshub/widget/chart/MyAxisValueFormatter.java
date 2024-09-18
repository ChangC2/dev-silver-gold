package seemesave.businesshub.widget.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.List;

public class MyAxisValueFormatter extends ValueFormatter {
    private List labels;

    public MyAxisValueFormatter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            int index = (int) value;
            return String.valueOf(labels.get(index));
        } catch (Exception e) {
            return "";
        }
    }
}