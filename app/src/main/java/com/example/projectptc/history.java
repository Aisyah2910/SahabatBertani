package com.example.projectptc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {
    private RecyclerView recyclerView;
    private History_Adapter historyAdapter;
    private List<HistoryBaglog> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyList = new ArrayList<>();
        historyAdapter = new History_Adapter(historyList); // Menyambungkan data dengan adapter
        recyclerView.setAdapter(historyAdapter);

        loadHistoryData();

        ImageView kembali = findViewById(R.id.kembali);
        kembali.setOnClickListener(view -> {
            Intent intent = new Intent(history.this, TampilanAplikasi.class);
            startActivity(intent);
        });
    }

    private void loadHistoryData() {
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("history");
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                if (!snapshot.exists()) {
                    Toast.makeText(history.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataSnapshot data : snapshot.getChildren()) {
                    String id = data.getKey(); // Mendapatkan ID unik dari tiap data
                    HistoryBaglog item = data.getValue(HistoryBaglog.class);
                    if (item != null) {
                        item.setId(id); // Menyimpan ID ke dalam item
                        historyList.add(item);
                    }
                }
                historyAdapter.notifyDataSetChanged(); // Memberitahu adapter untuk memperbarui data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(history.this, "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
