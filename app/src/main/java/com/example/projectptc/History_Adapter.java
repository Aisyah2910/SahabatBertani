package com.example.projectptc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.HistoryViewHolder> {
    private List<HistoryBaglog> historyList;

    public History_Adapter(List<HistoryBaglog> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryBaglog currentItem = historyList.get(position);
        holder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0; // Hindari NullPointerException
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRakName, textViewJumlahBaglog, textViewTanggalMulai, textViewTanggalPanen, textViewStatus;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            textViewRakName = itemView.findViewById(R.id.textViewRakName);
            textViewJumlahBaglog = itemView.findViewById(R.id.textViewJumlahBaglog);
            textViewTanggalMulai = itemView.findViewById(R.id.textViewTanggalMulai);
            textViewTanggalPanen = itemView.findViewById(R.id.textViewTanggalPanen);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }

        public void bind(HistoryBaglog item) {
            textViewRakName.setText("Nama Rak: " + item.getRak_name());
            textViewTanggalMulai.setText("Tanggal Mulai: " + item.getTanggal_mulai());
            textViewTanggalPanen.setText("Tanggal Panen: " + item.getTanggal_panen());
            textViewJumlahBaglog.setText("Jumlah Baglog: " + item.getJumlah_baglog());
            textViewStatus.setText("Status:" + item.getStatus());
        }
    }
}
