package com.example.projectptc;

import java.text.SimpleDateFormat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class TampilanAplikasi extends AppCompatActivity {
    private TextView text7, text8, text9;
    private Switch manualSwitch;
    private DatabaseReference firebaseDatabase;
    private DatabaseReference fd;
    private DatabaseReference pompa;
    private DatabaseReference sensor1;
    private DatabaseReference sensor2;
    private DatabaseReference jadwalRef;
    private DatabaseReference historyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_aplikasi);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("sensor");
        fd = FirebaseDatabase.getInstance().getReference("baglogs");
        pompa = FirebaseDatabase.getInstance().getReference("pompa/status");
        sensor1 = FirebaseDatabase.getInstance().getReference("sensor/sensor_1");
        sensor2 = FirebaseDatabase.getInstance().getReference("sensor/sensor_2");
        jadwalRef = FirebaseDatabase.getInstance().getReference("JadwalPenyiraman");
        historyRef = FirebaseDatabase.getInstance().getReference("history");

        testDatabase();
        initUI();

        ImageView history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TampilanAplikasi.this, history.class);
                startActivity(intent);
            }
        });

        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);
        text9 = findViewById(R.id.text9);
        fetchDataFromFirebase();

        manualSwitch = findViewById(R.id.SwtchMnl);
        manualSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pompa.setValue(true);
                } else {
                    pompa.setValue(false);
                }
            }
        });
    }

    private void fetchDataFromFirebase() {
        jadwalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String penyiraman1 = dataSnapshot.child("Penyiraman1").getValue(String.class);
                String penyiraman2 = dataSnapshot.child("Penyiraman2").getValue(String.class);
                String penyiraman3 = dataSnapshot.child("Penyiraman3").getValue(String.class);

                if (penyiraman1 != null) {
                    text7.setText(" Penyiraman pertama: " + penyiraman1);
                } else {
                    text7.setText(" Penyiraman pertama: Tidak ada data");
                }

                if (penyiraman2 != null) {
                    text8.setText(" Penyiraman kedua: " + penyiraman2);
                } else {
                    text8.setText(" Penyiraman kedua: Tidak ada data");
                }

                if (penyiraman3 != null) {
                    text9.setText(" Penyiraman ketiga: " + penyiraman3);
                } else {
                    text9.setText(" Penyiraman ketiga: Tidak ada data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TampilanAplikasi.this, "Gagal mengambil data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        initHumidityTemperatureListeners();

        ImageView mappingImage = findViewById(R.id.mapping_image);
        mappingImage.setOnTouchListener(this::handleTouchMapping);
    }

    private void initHumidityTemperatureListeners() {
        ImageView shape3 = findViewById(R.id.shape3);
        ImageView icon2 = findViewById(R.id.icon2);
        TextView text2 = findViewById(R.id.text2);
        ImageView shape2 = findViewById(R.id.shape2);
        TextView data1 = findViewById(R.id.data1);
        TextView data2 = findViewById(R.id.data2);
        ImageView icon1 = findViewById(R.id.icon1);
        TextView text1 = findViewById(R.id.text1);

        View.OnClickListener toKelembapan = v -> {
            Intent intent = new Intent(TampilanAplikasi.this, Catatan_Kelembapan.class);
            startActivity(intent);
        };

        View.OnClickListener toSuhu = v -> {
            Intent intent = new Intent(TampilanAplikasi.this, Catatan_Suhu.class);
            startActivity(intent);
        };

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalSuhu = 0;
                float totalKelembapan = 0;
                int sensorCount = 0;

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (childSnapshot.hasChild("temperature") && childSnapshot.hasChild("humidity")) {
                        float suhu = childSnapshot.child("temperature").getValue(Float.class);
                        float kelembapan = childSnapshot.child("humidity").getValue(Float.class);

                        totalSuhu += suhu;
                        totalKelembapan += kelembapan;
                        sensorCount++;
                    }
                }

                if (sensorCount > 0) {
                    float rataSuhu = totalSuhu / sensorCount;
                    float rataKelembapan = totalKelembapan / sensorCount;

                    data2.setText(String.format("%.2f°C", rataSuhu));
                    data1.setText(String.format("%.2f%%", rataKelembapan));
                } else {
                    data2.setText("No Data");
                    data1.setText("No Data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                data2.setText("Error");
                data1.setText("Error");
            }
        });

        shape2.setOnClickListener(toKelembapan);
        data1.setOnClickListener(toKelembapan);
        icon1.setOnClickListener(toKelembapan);
        text1.setOnClickListener(toKelembapan);

        shape3.setOnClickListener(toSuhu);
        data2.setOnClickListener(toSuhu);
        icon2.setOnClickListener(toSuhu);
        text2.setOnClickListener(toSuhu);
    }

    private boolean handleTouchMapping(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            Log.d("Touch Position", "X: " + x + ", Y: " + y);

            if (isInsideCircle(x, y, 721, 954, 28)) {
                fetchSensor1Data();
            } else if (isInsideCircle(x, y, 721, 84, 28)) {
                fetchSensor2Data();
            } else if (isInsideCircle(x, y, 164, 84, 28) || isInsideCircle(x, y, 373, 84, 28) ||
                    isInsideCircle(x, y, 601, 84, 28) || isInsideCircle(x, y, 156, 954, 28) ||
                    isInsideCircle(x, y, 377, 954, 28) || isInsideCircle(x, y, 607, 954, 28)) {
                showPopup("Spray", "Spray digunakan untuk menyiram rak baglog secara otomatis.");
            } else if (isInsideRectangle(x, y, 220, 117, 292, 887)) {
                handleRakBaglogInfo("Rak Baglog 1");
            } else if (isInsideRectangle(x, y, 447, 117, 524, 762)) {
                handleRakBaglogInfo("Rak Baglog 2");
            } else if (isInsideRectangle(x, y, 679, 117, 751, 762)) {
                handleRakBaglogInfo("Rak Baglog 3");
            }
        }
        return true;
    }

    private void fetchSensor2Data() {
        sensor2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float suhu2 = snapshot.child("temperature").getValue(Float.class);
                    float kelembapan2 = snapshot.child("humidity").getValue(Float.class);

                    showSensor2Popup("Informasi Sensor 2", "Suhu: " + suhu2 + "°C\nKelembapan: " + kelembapan2 + "%" );
                } else {
                    showSensor2Popup("Informasi Sensor 2", "Data tidak tersedia.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showPopup("Error", "Gagal mengambil data: " + error.getMessage());
            }
        });
    }

    private void showSensor2Popup(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        TextView messageView = new TextView(this);
        messageView.setText(message);
        layout.addView(messageView);

        Space space = new Space(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50);
        layout.addView(space, spaceParams);

        builder.setView(layout);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void fetchSensor1Data() {
        sensor1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float suhu1 = snapshot.child("temperature").getValue(Float.class);
                    float kelembapan1 = snapshot.child("humidity").getValue(Float.class);

                    showSensor1Popup("Informasi Sensor 1", "Suhu: " + suhu1 + "°C\nKelembapan: " + kelembapan1 + "%" );
                } else {
                    showSensor1Popup("Informasi Sensor 1", "Data tidak tersedia.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showPopup("Error", "Gagal mengambil data: " + error.getMessage());
            }
        });
    }

    private void showSensor1Popup(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        TextView messageView = new TextView(this);
        messageView.setText(message);
        layout.addView(messageView);

        Space space = new Space(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50);
        layout.addView(space, spaceParams);

        builder.setView(layout);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void handleRakBaglogInfo(String rakName) {
        String rakKey = rakName.replace(" ", "_");

        fd.child(rakKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue(String.class);

                    if (status != null && status.equalsIgnoreCase("Penanaman")) {
                        showPopupRak(rakName, snapshot);
                    } else {
                        showInputDialog(rakName);
                    }
                } else {
                    showInputDialog(rakName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TampilanAplikasi.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopup(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showPopupRak(String rakName, DataSnapshot snapshot) {
        String jumlahBaglog = snapshot.child("jumlah_baglog").getValue(String.class);
        String tanggalMulai = snapshot.child("tanggal_mulai").getValue(String.class);
        String status = snapshot.child("status").getValue(String.class);
        Long usiaLong = snapshot.child("usia").getValue(Long.class);
        int usia = usiaLong != null ? usiaLong.intValue() : 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informasi " + rakName)
                .setMessage("Jumlah Baglog: " + jumlahBaglog +
                        "\nTanggal Mulai: " + tanggalMulai +
                        "\nUsia: " + usia + " hari" +
                        "\nStatus: " + status)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Panen", (dialog, which) -> {
                    updateRakBaglogStatusToSelesai(rakName, snapshot);
                })
                .show();
    }

    private void updateRakBaglogStatusToSelesai(String rakName, DataSnapshot snapshot) {
        String jumlahBaglog = snapshot.child("jumlah_baglog").getValue(String.class);
        String tanggalMulai = snapshot.child("tanggal_mulai").getValue(String.class);
        String status = "Selesai";
        Long usiaLong = snapshot.child("usia").getValue(Long.class);
        int usia = usiaLong != null ? usiaLong.intValue() : 0;

        String tanggalPanen = getCurrentDate();

        HashMap<String, Object> data = new HashMap<>();
        data.put("rak_name", rakName);
        data.put("jumlah_baglog", jumlahBaglog);
        data.put("tanggal_mulai", tanggalMulai);
        data.put("tanggal_panen", tanggalPanen);
        data.put("status", status);
        data.put("usia", usia);

        String historyId = historyRef.push().getKey();
        if (historyId != null) {
            historyRef.child(historyId).setValue(data)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("baglogs").child(rakName.replace(" ", "_"))
                                    .removeValue()
                                    .addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            Toast.makeText(TampilanAplikasi.this, "Panen selesai, data dipindahkan ke histori!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(TampilanAplikasi.this, "Gagal menghapus data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(TampilanAplikasi.this, "Gagal memindahkan data ke histori.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(TampilanAplikasi.this, "Gagal membuat ID untuk histori.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());
    }

    private void showInputDialog(String rakName) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.activity_popup_rak_bg, null);

        EditText inputJumlahBaglog = dialogView.findViewById(R.id.inputJumlahBaglog);
        Button selectTanggalMulai = dialogView.findViewById(R.id.selectTanggalMulai);
        TextView tanggalMulaiSelected = dialogView.findViewById(R.id.tanggalMulaiSelected);

        final Calendar calendar = Calendar.getInstance();
        selectTanggalMulai.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                tanggalMulaiSelected.setText("Tanggal Mulai: " + selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input untuk " + rakName)
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    String jumlahBaglogText = inputJumlahBaglog.getText().toString().trim();
                    String tanggalMulaiText = tanggalMulaiSelected.getText().toString().replace("Tanggal Mulai: ", "").trim();

                    if (jumlahBaglogText.isEmpty() || tanggalMulaiText.isEmpty()) {
                        Toast.makeText(this, "Semua input harus diisi!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int usia = calculateUsia(tanggalMulaiText);

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("rak_name", rakName);
                    data.put("jumlah_baglog", jumlahBaglogText);
                    data.put("tanggal_mulai", tanggalMulaiText);
                    data.put("status", "Penanaman");
                    data.put("usia", usia);

                    fd.child(rakName.replace(" ", "_")).setValue(data).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private int calculateUsia(String tanggalMulaiText) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = sdf.parse(tanggalMulaiText);
            Date today = new Date();
            long diffInMillis = today.getTime() - startDate.getTime();
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isInsideCircle(float x, float y, float cx, float cy, float radius) {
        float dx = x - cx;
        float dy = y - cy;
        return dx * dx + dy * dy <= radius * radius;
    }

    private boolean isInsideRectangle(float x, float y, float left, float top, float right, float bottom) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    private void testDatabase() {
        Log.d("Firebase", "Koneksi ke Firebase berhasil.");
    }
}
