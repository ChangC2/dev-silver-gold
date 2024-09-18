package seemesave.businesshub.view.vendor.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartStoreFragment extends BaseFragment {

    private View mFragView;

    @BindView(R.id.chart1)
    BarChart barChart;
    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;
    public ChartStoreFragment() {
    }

    public static ChartStoreFragment newInstance() {
        ChartStoreFragment fragment = new ChartStoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.fragment_chart_store, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        drawChart();
    }
    private void drawChart() {
        getBarEntries();
        barDataSet = new BarDataSet(barEntriesArrayList, "");

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(Color.parseColor("#6595F8"));

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        String[] xDataL = {"", "", "Week 1", "Week 2" , "Week 3" , "Week 4",  "Week 5",  "Week 6"};
        ArrayList<String> xEntrys = new ArrayList<>();
        for(int i = 1; i < xDataL.length; i++){
            xEntrys.add(xDataL[i]);
        }


        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xEntrys));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setFitBars(false);
        barChart.setClipValuesToContent(false);



    }
    private void getBarEntries() {
        barEntriesArrayList = new ArrayList<>();
        barEntriesArrayList.add(new BarEntry(1, 4));
        barEntriesArrayList.add(new BarEntry(2, 6));
        barEntriesArrayList.add(new BarEntry(3, 6));
        barEntriesArrayList.add(new BarEntry(4, 2));
        barEntriesArrayList.add(new BarEntry(5, 4));
        barEntriesArrayList.add(new BarEntry(6, 1));
    }

//    @OnClick({R.id.txtOrder, R.id.txtReadyCollect, R.id.txtCompleted})
//    public void onClickButtons(View view) {
//        switch (view.getId()) {
//            case R.id.txtOrder:
//                setPage(0);
//                break;
//            case R.id.txtReadyCollect:
//                setPage(1);
//                break;
//            case R.id.txtCompleted:
//                setPage(2);
//                break;
//        }
//    }
}