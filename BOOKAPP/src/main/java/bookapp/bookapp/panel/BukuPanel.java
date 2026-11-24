package bookapp.bookapp.panel;

import bookapp.bookapp.entity.Buku;
import bookapp.bookapp.model.BukuModel;
import java.beans.Beans;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import bookapp.bookapp.repo.CartRepo;
import bookapp.bookapp.repo.AuthRepo;
import bookapp.bookapp.entity.Log;
import bookapp.bookapp.entity.Users;
import bookapp.bookapp.model.LogModel;
import java.time.LocalDateTime;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author gori
 */
public class BukuPanel extends javax.swing.JPanel {

    private final BukuModel bukuModel = new BukuModel();
    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
    private CartRepo cartRepo = CartRepo.getInstance();
    private List<Buku> currentList = new ArrayList<>();
    private Buku selectedBuku = null;
    private Runnable onCartUpdated;
    private final LogModel logModel = new LogModel();
    
    /**
     * Creates new form BukuPanel
     */
    public BukuPanel() {
        initComponents();
        setupPanel();
        setAdminMode(AuthRepo.getInstance().isAdmin());
    }
    
    public void setAdminMode(boolean admin) {
    btnCreate.setVisible(admin);
    btnUpdate.setVisible(admin);
    btnDelete.setVisible(admin);
    btnTambah.setVisible(!admin);
    
    txtJudul.setEditable(admin);
    txtPenulis.setEditable(admin);
    txtTahunTerbit.setEditable(admin);
    txtStok.setEditable(admin);
    txtHarga.setEditable(admin);
    } 
    
    private void setupPanel() {
        txtSearch.setText("");
        bukuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Populate form fields whenever a table row is selected
        bukuTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onRowSelected();
            }
        });

        // Skip DB queries inside NetBeans GUI Designer
        if (!Beans.isDesignTime()) {
            refreshTable();
        }
    }


    private void catatLog(int idBuku, Log.typeAksi type, String desc) {
        Users user = AuthRepo.getInstance().getCurrentUser();
        if (user == null) return;
        try {
            logModel.create(new Log(0, user.getIdUser(), user.getUsername(),
                    idBuku, LocalDateTime.now(), desc, type));
        } catch (RuntimeException e) {
            System.err.println("Gagal mencatat log: " + e.getMessage());
        }
    }

    private String buildDiff(Buku lama, Buku baru) {
        StringBuilder sb = new StringBuilder();
        if (!lama.getJudul().equals(baru.getJudul()))
            sb.append("judul: \"").append(lama.getJudul()).append("\" -> \"").append(baru.getJudul()).append("\"; ");
        if (!lama.getNamaPenulis().equals(baru.getNamaPenulis()))
            sb.append("penulis: ").append(lama.getNamaPenulis()).append(" -> ").append(baru.getNamaPenulis()).append("; ");
        if (Double.compare(lama.getHarga(), baru.getHarga()) != 0)
            sb.append("harga: ").append(formatPlainNumber(lama.getHarga())).append(" -> ").append(formatPlainNumber(baru.getHarga())).append("; ");
        if (lama.getStock() != baru.getStock())
            sb.append("stok: ").append(lama.getStock()).append(" -> ").append(baru.getStock()).append("; ");
        if (lama.getTahunTerbit() != baru.getTahunTerbit())
            sb.append("tahun: ").append(lama.getTahunTerbit()).append(" -> ").append(baru.getTahunTerbit()).append("; ");
        return sb.toString();
    }

    public void setCartRepo(CartRepo cartRepo) {
        if (cartRepo != null) {
            this.cartRepo = cartRepo;
        }
    }

    public void setOnCartUpdated(Runnable onCartUpdated) {
        this.onCartUpdated = onCartUpdated;
    }

    public void refreshTable() {
        try {
            populateTable(bukuModel.readAll());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memuat data buku. Pastikan MySQL aktif.\nDetail: " + e.getMessage(),
                    "Error Database",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void populateTable(List<Buku> books) {
        currentList = (books != null) ? books : new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) bukuTable.getModel();
        model.setRowCount(0);

        int no = 1;
        for (Buku b : currentList) {
            model.addRow(new Object[]{
                no++,
                b.getJudul(),
                b.getNamaPenulis(),
                rupiahFormat.format(b.getHarga()),
                b.getStock(),
                b.getTahunTerbit()
            });
        }
        clearForm();
    }

    private void onRowSelected() {
        int row = bukuTable.getSelectedRow();
        if (row < 0 || row >= currentList.size()) {
            selectedBuku = null;
            return;
        }

        selectedBuku = currentList.get(row);
        txtJudul.setText(selectedBuku.getJudul());
        txtPenulis.setText(selectedBuku.getNamaPenulis());
        txtTahunTerbit.setText(String.valueOf(selectedBuku.getTahunTerbit()));
        txtStok.setText(String.valueOf(selectedBuku.getStock()));
        txtHarga.setText(formatPlainNumber(selectedBuku.getHarga()));
    }

    private String formatPlainNumber(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }

    private void clearForm() {
        selectedBuku = null;
        bukuTable.clearSelection();
        txtJudul.setText("");
        txtPenulis.setText("");
        txtTahunTerbit.setText("");
        txtStok.setText("");
        txtHarga.setText("");
    }

    private Buku buildBukuFromForm(int idBuku) {
        String judul = txtJudul.getText().trim();
        String penulis = txtPenulis.getText().trim();
        String tahunStr = txtTahunTerbit.getText().trim();
        String stokStr = txtStok.getText().trim();
        String hargaStr = txtHarga.getText().trim();

        if (judul.isEmpty() || penulis.isEmpty() || tahunStr.isEmpty() || stokStr.isEmpty() || hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field buku wajib diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int tahunTerbit;
        try {
            tahunTerbit = Integer.parseInt(tahunStr);
            if (tahunTerbit < 1000 || tahunTerbit > 9999) {
                JOptionPane.showMessageDialog(this, "Tahun terbit harus berupa 4 digit angka (contoh: 2024).", "Validasi", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tahun terbit harus berupa angka.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int stok;
        try {
            stok = Integer.parseInt(stokStr);
            if (stok < 0) {
                JOptionPane.showMessageDialog(this, "Stok tidak boleh negatif.", "Validasi", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stok harus berupa angka bulat.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        double harga;
        try {
            harga = Double.parseDouble(hargaStr);
            if (harga < 0) {
                JOptionPane.showMessageDialog(this, "Harga tidak boleh negatif.", "Validasi", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka (contoh: 85000).", "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Buku(idBuku, judul, penulis, harga, stok, tahunTerbit);
    }

    private void performSearch() {
        String keyword = txtSearch.getText().trim();
        try {
            List<Buku> result = keyword.isEmpty()
                    ? bukuModel.readAll()
                    : bukuModel.search(keyword);
            populateTable(result);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencari buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnSearch.setSelected(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        bukuTable = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JToggleButton();
        labelJudul = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        labelPenulis = new javax.swing.JLabel();
        txtPenulis = new javax.swing.JTextField();
        labelTahunTerbit = new javax.swing.JLabel();
        txtTahunTerbit = new javax.swing.JTextField();
        labelStok = new javax.swing.JLabel();
        txtStok = new javax.swing.JTextField();
        labelHarga = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();

        bukuTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Judul", "Penulis", "Harga", "Stok", "Tahun Terbit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane.setViewportView(bukuTable);

        txtSearch.setText("jTextField1");
        txtSearch.setPreferredSize(new java.awt.Dimension(300, 26));
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        labelJudul.setText("Judul Buku");

        labelPenulis.setText("Penulis Buku");

        txtPenulis.setPreferredSize(new java.awt.Dimension(1, 26));

        labelTahunTerbit.setText("Tahun Terbit");

        labelStok.setText("Stok Buku");

        txtStok.setPreferredSize(new java.awt.Dimension(1, 26));

        labelHarga.setText("Harga Buku");

        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);

        btnDelete.setText("DELETE");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        btnCreate.setText("CREATE");
        btnCreate.addActionListener(this::btnCreateActionPerformed);

        btnTambah.setText("Tambah ke Keranjang");
        btnTambah.addActionListener(this::btnTambahActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtJudul)
                            .addComponent(labelJudul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPenulis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelPenulis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTahunTerbit)
                            .addComponent(labelTahunTerbit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHarga)
                            .addComponent(labelHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTambah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelJudul)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelPenulis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPenulis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelTahunTerbit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelStok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCreate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelHarga)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        performSearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        performSearch();
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if (selectedBuku == null) {
            JOptionPane.showMessageDialog(this, "Pilih buku dari tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Re-read latest stock from DB so cart checks stay accurate
        Buku freshBuku = bukuModel.findById(selectedBuku.getIdBuku());
        if (freshBuku == null) {
            JOptionPane.showMessageDialog(this, "Data buku tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
            refreshTable();
            return;
        }

        int alreadyInCart = cartRepo.getQuantityByBookId(freshBuku.getIdBuku());
        int sisaStok = freshBuku.getStock() - alreadyInCart;

        if (freshBuku.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Stok buku \"" + freshBuku.getJudul() + "\" habis.", "Stok Habis", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (sisaStok <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Semua stok buku \"" + freshBuku.getJudul() + "\" (" + alreadyInCart + ") sudah ada di keranjang.",
                    "Stok Maksimal",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String input = (String) JOptionPane.showInputDialog(
                this,
                "Masukkan jumlah untuk \"" + freshBuku.getJudul() + "\"\n(Stok tersedia untuk ditambah: " + sisaStok + ")",
                "Tambah ke Keranjang",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "1"
        );

        if (input == null) {
            return; // User cancelled
        }

        int jumlah;
        try {
            jumlah = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka bulat.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (jumlah > sisaStok) {
            JOptionPane.showMessageDialog(
                    this,
                    "Jumlah melebihi sisa stok tersedia (" + sisaStok + ").",
                    "Stok Tidak Cukup",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        cartRepo.addItem(freshBuku, jumlah);

        if (onCartUpdated != null) {
            onCartUpdated.run();
        }

        JOptionPane.showMessageDialog(
                this,
                jumlah + "x \"" + freshBuku.getJudul() + "\" berhasil ditambahkan ke keranjang.\n"
                        + "Total keranjang saat ini: " + rupiahFormat.format(cartRepo.total()),
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        Buku bukuBaru = buildBukuFromForm(0);
        if (bukuBaru == null) {
            return;
        }

        try {
        bukuModel.create(bukuBaru);
        catatLog(bukuBaru.getIdBuku(), Log.typeAksi.CREATE,
                "Menambah buku \"" + bukuBaru.getJudul() + "\" (" + bukuBaru.getNamaPenulis()
                + ", harga " + formatPlainNumber(bukuBaru.getHarga()) + ", stok " + bukuBaru.getStock() + ")");
            
            JOptionPane.showMessageDialog(this, "Buku \"" + bukuBaru.getJudul() + "\" berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal Menambahkan Buku", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (selectedBuku == null) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin diubah dari tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Buku lama = selectedBuku;
        Buku bukuUpdate = buildBukuFromForm(lama.getIdBuku());
        if (bukuUpdate == null) {
            return;
        }

        try {
            bukuModel.update(bukuUpdate);
            String diff = buildDiff(lama, bukuUpdate);
            catatLog(bukuUpdate.getIdBuku(), Log.typeAksi.UPDATE,
                    "Mengubah buku \"" + lama.getJudul() + "\": " + (diff.isEmpty() ? "tidak ada perubahan" : diff));
            JOptionPane.showMessageDialog(this, "Data buku berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal Mengubah Buku", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        Buku dihapus = selectedBuku;
        if (selectedBuku == null) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dihapus dari tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus buku \"" + selectedBuku.getJudul() + "\"?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            bukuModel.delete(dihapus.getIdBuku());
            catatLog(0, Log.typeAksi.DELETE,
                    "Menghapus buku #" + dihapus.getIdBuku() + " \"" + dihapus.getJudul()
                    + "\" (" + dihapus.getNamaPenulis() + ")");
            JOptionPane.showMessageDialog(this, "Buku berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Buku gagal dihapus. Buku yang sudah tercatat di transaksi tidak bisa dihapus.\nDetail: " + e.getMessage(),
                    "Gagal Menghapus Buku",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JToggleButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JTable bukuTable;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JLabel labelHarga;
    private javax.swing.JLabel labelJudul;
    private javax.swing.JLabel labelPenulis;
    private javax.swing.JLabel labelStok;
    private javax.swing.JLabel labelTahunTerbit;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtPenulis;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStok;
    private javax.swing.JTextField txtTahunTerbit;
    // End of variables declaration//GEN-END:variables
}
