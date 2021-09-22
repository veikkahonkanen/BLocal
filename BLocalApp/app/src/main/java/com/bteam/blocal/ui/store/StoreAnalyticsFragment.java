package com.bteam.blocal.ui.store;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bteam.blocal.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoreAnalyticsFragment extends Fragment {
    public StoreAnalyticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_analytics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Snackbar.make(getView(), R.string.lbl_stub_data, Snackbar.LENGTH_SHORT).show();
        setLineChart(view);
        setPieChart(view);

    }

    private void setLineChart(@NonNull View view) {
        List<Entry> entries = new ArrayList<>();
        Random rnd = new Random();
        int upperbound = 100;
        for (int i = 0; i < 20; i++) {
            entries.add(new Entry(i, rnd.nextInt(upperbound)));
        }
        LineDataSet dataSet = new LineDataSet(entries, this.getResources().getString(R.string.user_visits));
        dataSet.setLineWidth(3);
        dataSet.setCircleRadius(5);
        dataSet.setCircleHoleRadius(2.5f);
        dataSet.setCircleColor(Color.WHITE);
        dataSet.setColor(Color.WHITE);
        dataSet.setHighLightColor(Color.WHITE);
        dataSet.setDrawValues(false);

        LineData data = new LineData(dataSet);
        LineChart chart = (LineChart) view.findViewById(R.id.chart);

        data.setDrawValues(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setTouchEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
        chart.getAxisLeft().setDrawGridLines(false); // disable grid lines for the left YAxis
        chart.getAxisRight().setDrawGridLines(false); // disable grid lines for the right YAxis
        chart.getDescription().setEnabled(false);

        chart.setData(data);
    }

    private void setPieChart(View view){
        // Inspired by https://www.youtube.com/watch?v=MiVx3AQD_PI
        PieChart pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(34, this.getResources().getString(R.string.necessities)));
        pieEntries.add(new PieEntry(60, this.getResources().getString(R.string.electronics)));
        pieEntries.add(new PieEntry(34, this.getResources().getString(R.string.presents)));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, this.getResources().getString(R.string.outcome));

        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);

        pieChart.setData(pieData);
    }
}