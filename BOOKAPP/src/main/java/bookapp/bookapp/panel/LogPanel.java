/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package bookapp.bookapp.panel;

import bookapp.bookapp.entity.Buku;
import bookapp.bookapp.entity.Log;
import bookapp.bookapp.entity.Users;
import bookapp.bookapp.model.BukuModel;
import bookapp.bookapp.model.LogModel;
import bookapp.bookapp.model.UsersModel;
import java.beans.Beans;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author gori
 */
public class LogPanel extends javax.swing.JPanel {

    private final LogModel logModel = new LogModel();
    private final BukuModel bukuModel = new BukuModel();
    private final UsersModel usersModel = new UsersModel();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private boolean isUpdatingDates = false;

    public LogPanel() {
        initComponents();
        setupPanel();
    }

    private void setupPanel() {
        logTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"No.", "Tanggal", "Judul Buku", "Nama Perubah", "Type", "Deskripsi"}
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        // Tambah opsi "Transaksi Log" (ditaruh di sini supaya tidak ketimpa kode generated NetBeans)
        filterBox.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"All Log", "Create Log", "Update Log", "Delete Log", "Transaksi Log"}));
        dateDari.setDateFormatString("dd-MM-yyyy");
        dateSampai.setDateFormatString("dd-MM-yyyy");

        dateDari.addPropertyChangeListener("date", evt -> onDateChanged(true));
        dateSampai.addPropertyChangeListener("date", evt -> onDateChanged(false));

        // Skip query DB saat membuka form di NetBeans GUI Designer
        if (!Beans.isDesignTime()) {
            refreshData();
        }
    }

    private void onDateChanged(boolean dariChanged) {
        if (isUpdatingDates || Beans.isDesignTime()) {
            return;
        }

        LocalDate dari = toLocalDate(dateDari.getDate());
        LocalDate sampai = toLocalDate(dateSampai.getDate());

        if (dari != null && sampai != null && dari.isAfter(sampai)) {
            isUpdatingDates = true;
            try {
                if (dariChanged) {
                    dateSampai.setDate(dateDari.getDate());
                } else {
                    dateDari.setDate(dateSampai.getDate());
                }
            } finally {
                isUpdatingDates = false;
            }
        }

        refreshData();
    }

    public void refreshData() {
        if (Beans.isDesignTime()) {
            return;
        }

        try {
            Log.typeAksi typeFilter = parseFilterType((String) filterBox.getSelectedItem());
            LocalDate dari = toLocalDate(dateDari.getDate());
            LocalDate sampai = toLocalDate(dateSampai.getDate());

            List<Log> logs = logModel.findByFilter(typeFilter, dari, sampai);

            Map<Integer, String> bukuMap = buildBukuMap();

            DefaultTableModel model = (DefaultTableModel) logTable.getModel();
            model.setRowCount(0);

            int no = 1;
            for (Log log : logs) {
                String tanggal = log.getDatetime() != null
                        ? log.getDatetime().format(dateFormatter)
                        : "-";

                // idBuku == 0 berarti id_buku NULL di DB (buku sudah dihapus)
                String judulBuku = log.getIdBuku() == 0
                        ? "(buku dihapus)"
                        : bukuMap.getOrDefault(log.getIdBuku(), "Buku #" + log.getIdBuku());

                // username disimpan langsung di tabel log, jadi tetap terbaca walau akun sudah dihapus
                String namaPerubah = (log.getUsername() != null && !log.getUsername().isEmpty())
                        ? log.getUsername()
                        : "-";

                String tipe = log.getType() != null ? log.getType().name() : "-";

                model.addRow(new Object[]{
                    no++,
                    tanggal,
                    judulBuku,
                    namaPerubah,
                    tipe,
                    log.getDesc()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memuat data log. Pastikan MySQL aktif.\nDetail: " + e.getMessage(),
                    "Error Database",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Log.typeAksi parseFilterType(String selectedFilter) {
        if (selectedFilter == null || selectedFilter.equals("All Log")) {
            return null;
        }

        return switch (selectedFilter) {
            case "Create Log" -> Log.typeAksi.CREATE;
            case "Update Log" -> Log.typeAksi.UPDATE;
            case "Delete Log" -> Log.typeAksi.DELETE;
            case "Transaksi Log" -> Log.typeAksi.TRANSAKSI;
            default -> null;
        };
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Map<Integer, String> buildBukuMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Buku b : bukuModel.readAll()) {
            map.put(b.getIdBuku(), b.getJudul());
        }
        return map;
    }

//    private Map<Integer, String> buildUserMap() {
//        Map<Integer, String> map = new HashMap<>();
//        for (Users u : usersModel.readAll()) {
//            map.put(u.getIdUser(), u.getUsername());
//        }
//        return map;
//    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        logTable = new javax.swing.JTable();
        refresh = new javax.swing.JButton();
        filterBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        dateDari = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dateSampai = new com.toedter.calendar.JDateChooser();
        btnReset = new javax.swing.JButton();

        logTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Tanggal", "Judul Buku", "Nama Perubah", "Type", "Deskripsi"
            }
        ));
        jScrollPane.setViewportView(logTable);

        refresh.setText("Refresh");
        refresh.addActionListener(this::refreshActionPerformed);

        filterBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Log", "Create Log", "Update Log", "Delete Log" }));
        filterBox.addActionListener(this::filterBoxActionPerformed);

        jLabel2.setText("Dari");

        jLabel3.setText("Sampai");

        btnReset.setText("Reset");
        btnReset.addActionListener(this::btnResetActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(filterBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refresh))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateDari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateSampai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReset)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refresh)
                    .addComponent(filterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dateSampai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateDari, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        refreshData();
    }//GEN-LAST:event_refreshActionPerformed

    private void filterBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBoxActionPerformed
        if (!isUpdatingDates) {
            refreshData();
        }
    }//GEN-LAST:event_filterBoxActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        isUpdatingDates = true;
        try {
            dateDari.setDate(null);
            dateSampai.setDate(null);
        } finally {
            isUpdatingDates = false;
        }
        refreshData();
    }//GEN-LAST:event_btnResetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private com.toedter.calendar.JDateChooser dateDari;
    private com.toedter.calendar.JDateChooser dateSampai;
    private javax.swing.JComboBox<String> filterBox;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTable logTable;
    private javax.swing.JButton refresh;
    // End of variables declaration//GEN-END:variables
}
