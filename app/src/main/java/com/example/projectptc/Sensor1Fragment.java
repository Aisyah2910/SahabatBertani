package com.example.projectptc;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class Sensor1Fragment extends Fragment {

    private LineChart lineChart;
    private TextView selectedDateTextView;
    private TextView noDataTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor1, container, false);

        lineChart = view.findViewById(R.id.lineChart);
        Button selectDateButton = view.findViewById(R.id.selectDateButton);
        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
        noDataTextView = view.findViewById(R.id.noDataTextView);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = sdf.format(Calendar.getInstance().getTime());
        selectedDateTextView.setText("Tanggal: " + todayDate);

        fetchDataForDate(todayDate);
        selectDateButton.setOnClickListener(v -> showDatePicker());

        return view;
    }

    private void fetchDataForDate(String date) {
        List<Entry> entries = new ArrayList<>();
        DatabaseReference sensorDataRef = FirebaseDatabase.getInstance().getReference("sensor_log/1");
        sensorDataRef.orderByKey().limitToLast(100000).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entries.clear();
                float previousTime = -1;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String timestamp = dataSnapshot.getKey();
                    String Tanggal = timestamp.split(" ")[0];

                    if (Tanggal.startsWith(date)) {
                        try {
                            Float humidity = dataSnapshot.child("humidity").getValue(Float.class);
                            if (timestamp != null && humidity != null) {
                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(timestamp);
                                if (date != null) {
                                    float time = date.getTime() / 1000f;
                                    if (previousTime == -1 || time - previousTime >= 900) {
                                        entries.add(new Entry(time, humidity));
                                        previousTime = time;
                                    }
                                }
                            }
                        } catch (ParseException | NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (entries.isEmpty()) {
                    noDataTextView.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.GONE);
                } else {
                    noDataTextView.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);

                    LineDataSet lineDataSet = new LineDataSet(entries, "Humidity");
                    lineDataSet.setColor(Color.parseColor("#87CEEB"));
                    lineDataSet.setValueTextColor(Color.BLACK);
                    lineDataSet.setLineWidth(2f);
                    lineDataSet.setCircleColor(Color.parseColor("#FF6347"));
                    lineDataSet.setCircleRadius(4f);

                    LineData lineData = new LineData(lineDataSet);
                    lineChart.setData(lineData);

                    lineChart.getDescription().setEnabled(false);
                    lineChart.getAxisRight().setEnabled(false);

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(true);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            long timeInMillis = (long) value * 1000L;
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            return sdf.format(new Date(timeInMillis));
                        }
                    });

                    lineChart.setDragEnabled(true);
                    lineChart.setScaleEnabled(true);
                    lineChart.setPinchZoom(true);
                    lineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    String selected = "Tanggal: " + selectedDate;
                    selectedDateTextView.setText(selected);

                    fetchDataForDate(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }
}