package com.example.projectptc;

public class HistoryBaglog {
    private String Historyid;
    private String rak_name;
    private String jumlah_baglog;
    private String tanggal_mulai;
    private String status;
    private int usia;
    private String tanggal_panen;

    // Constructor kosong untuk Firebase
    public HistoryBaglog() {}

    public HistoryBaglog(String historyId, String rak_name, String jumlah_baglog, String tanggal_mulai, String tanggal_panen, String status, int usia) {
        this.Historyid = historyId;
        this.rak_name = rak_name;
        this.jumlah_baglog = jumlah_baglog;
        this.tanggal_mulai = tanggal_mulai;
        this.status = status;
        this.usia = usia;
        this.tanggal_panen = tanggal_panen;
    }

    public String getId() { return Historyid; }
    public void setId(String id) { this.Historyid = id; }

    public String getRak_name() { return rak_name; }
    public void seRak_name(String rak_name) { this.rak_name = rak_name; }

    public String getJumlah_baglog() { return jumlah_baglog; }
    public void setJumlah_baglog(String jumlah_baglog) { this.jumlah_baglog = jumlah_baglog; }

    public String getTanggal_mulai() { return tanggal_mulai; }
    public void setTanggal_mulai(String tanggal_mulai) { this.tanggal_mulai = tanggal_mulai; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getUsia() { return usia; }
    public void setUsia(int usia) { this.usia = usia; }

    public String getTanggal_panen() { return tanggal_panen; }
    public void setTanggal_panen(String tanggal_panen) { this.tanggal_panen = tanggal_panen; }
}
