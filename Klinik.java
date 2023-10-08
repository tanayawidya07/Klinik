import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Klinik {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNama, txtNIK, txtTanggalLahir, txtAlamat;
    private JButton btnTambah, btnUpdate, btnHapus, btnDaftarPasien, btnKeluar;
    private ArrayList<Pasien> daftarPasien = new ArrayList<>();
    private int currentIndex = -1;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Klinik window = new Klinik();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Klinik() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Aplikasi Klinik ");
        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);
        frame.setBounds(90, 90, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"No", "Nama Pasien", "NIK", "Tanggal Lahir", "Alamat"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panelInput = new JPanel();
        frame.getContentPane().add(panelInput, BorderLayout.PAGE_START);
        panelInput.setLayout(new GridLayout(4, 4));

        panelInput.add(new JLabel("                          Nama Pasien:"));
        txtNama = new JTextField();
        txtNama.setBackground(Color.lightGray);
        panelInput.add(txtNama);

        panelInput.add(new JLabel("                         NIK:"));
        txtNIK = new JTextField();
        txtNIK.setBackground(Color.lightGray);
        panelInput.add(txtNIK);

        panelInput.add(new JLabel("                         Tanggal Lahir (YYYY-MM-DD):"));
        txtTanggalLahir = new JTextField();
        txtTanggalLahir.setBackground(Color.lightGray);
        panelInput.add(txtTanggalLahir);

        panelInput.add(new JLabel("                         Alamat:"));
        txtAlamat = new JTextField();
        txtAlamat.setBackground(Color.lightGray);
        panelInput.add(txtAlamat);

        btnTambah = new JButton("Tambah Pasien");
        btnUpdate = new JButton("Update ");
        btnHapus = new JButton("Hapus ");
        btnDaftarPasien = new JButton("Daftar Pasien");
        btnKeluar = new JButton("Keluar");

        JPanel panelButtons = new JPanel();
        panelInput.setBackground(Color.GRAY);
        panelButtons.setBackground(Color.gray);
        panelButtons.add(btnTambah);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnHapus);
        panelButtons.add(btnDaftarPasien);
        panelButtons.add(btnKeluar);

        frame.getContentPane().add(panelButtons, BorderLayout.SOUTH);

        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tambahPasien();
                btnTambah.setBackground(Color.lightGray);
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePasien();
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hapusPasien();
            }
        });



        btnDaftarPasien.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tampilkanDaftarPasien();
            }
        });

        btnKeluar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void tambahPasien() {
        String nama = txtNama.getText();
        String nik = txtNIK.getText();
        String tanggalLahir = txtTanggalLahir.getText();
        String alamat = txtAlamat.getText();

        if (nama.isEmpty() || nik.isEmpty() || tanggalLahir.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(tanggalLahir);
            Pasien pasien = new Pasien(nama, nik, parsedDate, alamat);

            // Cek apakah NIK sudah ada di daftar
            if (daftarPasien.stream().anyMatch(p -> p.getNIK().equals(nik))) {
                JOptionPane.showMessageDialog(frame, "NIK sudah ada di dalam daftar.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            daftarPasien.add(pasien);
            updateTable();
            clearInputFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Format tanggal salah. Gunakan format YYYY-MM-DD.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updatePasien() {
        if (currentIndex < 0 || currentIndex >= daftarPasien.size()) {
            JOptionPane.showMessageDialog(frame, "Pilih pasien yang akan diupdate.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nama = txtNama.getText();
        String nik = txtNIK.getText();
        String tanggalLahir = txtTanggalLahir.getText();
        String alamat = txtAlamat.getText();

        if (nama.isEmpty() || nik.isEmpty() || tanggalLahir.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Semua kolom harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(tanggalLahir);
            Pasien pasien = daftarPasien.get(currentIndex);
            pasien.setNama(nama);
            pasien.setNIK(nik);
            pasien.setTanggalLahir(parsedDate);
            pasien.setAlamat(alamat);

            updateTable();
            clearInputFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Format tanggal salah. Gunakan format YYYY-MM-DD.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void hapusPasien() {
        if (currentIndex < 0 || currentIndex >= daftarPasien.size()) {
            JOptionPane.showMessageDialog(frame, "Pilih pasien yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Anda yakin ingin menghapus pasien ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            daftarPasien.remove(currentIndex);
            currentIndex = -1;
            updateTable();
            clearInputFields();
        }
    }

    private void prevRecord() {
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentRecord();
        }
    }

    private void nextRecord() {
        if (currentIndex < daftarPasien.size() - 1) {
            currentIndex++;
            displayCurrentRecord();
        }
    }

    private void tampilkanDaftarPasien() {
        JFrame daftarFrame = new JFrame("Daftar Pasien");
        daftarFrame.setBounds(120, 120, 600, 400);

        DefaultTableModel daftarModel = new DefaultTableModel(new Object[]{"No", "Nama Pasien", "NIK", "Tanggal Lahir", "Alamat"}, 0);
        JTable daftarTable = new JTable(daftarModel);
        JScrollPane daftarScrollPane = new JScrollPane(daftarTable);
        daftarFrame.getContentPane().add(daftarScrollPane, BorderLayout.CENTER);

        for (int i = 0; i < daftarPasien.size(); i++) {
            Pasien pasien = daftarPasien.get(i);
            Vector<Object> row = new Vector<>();
            row.add(i + 1);
            row.add(pasien.getNama());
            row.add(pasien.getNIK());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
            row.add(dateFormat.format(pasien.getTanggalLahir()));
            row.add(pasien.getAlamat());
            daftarModel.addRow(row);
        }

        JButton btnKeluarDaftar = new JButton("Keluar");
        btnKeluarDaftar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                daftarFrame.dispose();
            }
        });

        JPanel panelDaftarButtons = new JPanel();
        panelDaftarButtons.add(btnKeluarDaftar);
        daftarFrame.getContentPane().add(panelDaftarButtons, BorderLayout.SOUTH);

        daftarFrame.setVisible(true);
    }

    private void updateTable() {
        model.setRowCount(0);

        for (int i = 0; i < daftarPasien.size(); i++) {
            Pasien pasien = daftarPasien.get(i);
            Vector<Object> row = new Vector<>();
            row.add(i + 1);
            row.add(pasien.getNama());
            row.add(pasien.getNIK());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
            row.add(dateFormat.format(pasien.getTanggalLahir()));
            row.add(pasien.getAlamat());
            model.addRow(row);
        }
    }

    private void clearInputFields() {
        txtNama.setText("");
        txtNIK.setText("");
        txtTanggalLahir.setText("");
        txtAlamat.setText("");
    }

    private void displayCurrentRecord() {
        if (currentIndex >= 0 && currentIndex < daftarPasien.size()) {
            Pasien pasien = daftarPasien.get(currentIndex);
            txtNama.setText(pasien.getNama());
            txtNIK.setText(pasien.getNIK());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            txtTanggalLahir.setText(dateFormat.format(pasien.getTanggalLahir()));
            txtAlamat.setText(pasien.getAlamat());
        }
    }

    private class Pasien {
        private String nama;
        private String nik;
        private Date tanggalLahir;
        private String alamat;

        public Pasien(String nama, String nik, Date tanggalLahir, String alamat) {
            this.nama = nama;
            this.nik = nik;
            this.tanggalLahir = tanggalLahir;
            this.alamat = alamat;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getNIK() {
            return nik;
        }

        public void setNIK(String nik) {
            this.nik = nik;
        }

        public Date getTanggalLahir() {
            return tanggalLahir;
        }

        public void setTanggalLahir(Date tanggalLahir) {
            this.tanggalLahir = tanggalLahir;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }
    }
}