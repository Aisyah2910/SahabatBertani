package com.example.projectptc;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SensorAdapter extends FragmentStateAdapter {

    public SensorAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Sensor1Fragment();
            case 1:
                return new Sensor2Fragment();
            default:
                return new Sensor1Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
