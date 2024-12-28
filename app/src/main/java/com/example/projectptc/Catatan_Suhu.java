package com.example.projectptc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Catatan_Suhu extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SensorSuhuAdapter sensorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan_suhu);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        sensorAdapter = new SensorSuhuAdapter(this);
        viewPager.setAdapter(sensorAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Sensor 1");
                                break;
                            case 1:
                                tab.setText("Sensor 2");
                                break;
                        }
                    }
                }).attach();


        ImageView kembali = findViewById(R.id.kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Catatan_Suhu.this, TampilanAplikasi.class);
                startActivity(intent);
            }
        });
    }
}
