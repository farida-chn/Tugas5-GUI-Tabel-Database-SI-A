// TUGAS 5 PEMROGRAMAN LANJUT SI A
// Buat aplikasi GUI untuk operasi menampilkan dan menyimpan data dari suatu tabel database

// Nama : Farida Choirun Nisa 
// NIM  : 235150401111001

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Tabel_Database_Pasien_RS extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private final String patientDataFilePath = "D:\\PEMROGRAMAN LANJUT SI A\\FINALLLL CASE DATABASEE\\table_pasien\\pasien.csv";

    public Tabel_Database_Pasien_RS() {
        setTitle("TABEL DATABASE PASIEN - RS FARIDA SEHAT");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Membuat panel utama dengan latar belakang pink muda
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255, 182, 193)); // Pink muda
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout());

        // Membuat label untuk judul
        JLabel label = new JLabel("TABEL DATABASE PASIEN - RS FARIDA SEHAT", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(label, BorderLayout.NORTH);

        // Membuat tabel dengan model DefaultTableModel
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Membuat semua sel dapat diedit
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Serif", Font.PLAIN, 14));
        table.setRowHeight(20);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(Color.YELLOW);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Membuat panel tombol dengan layout FlowLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        // Membuat tombol tampilkan tabel
        JButton loadButton = new JButton("TAMPILKAN TABEL");
        loadButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(loadButton);

        // Membuat tombol simpan tabel
        JButton saveButton = new JButton("SIMPAN TABEL");
        saveButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(saveButton);

        // Membuat tombol tambah baris
        JButton addButton = new JButton("TAMBAH BARIS");
        addButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(addButton);

        // Membuat tombol hapus baris
        JButton deleteButton = new JButton("HAPUS BARIS");
        deleteButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Menambahkan action listener untuk tombol load
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadTableData();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Tabel_Database_Pasien_RS.this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Menambahkan action listener untuk tombol save
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveTableData();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Tabel_Database_Pasien_RS.this, "Error writing to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Menambahkan action listener untuk tombol add
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(new Object[]{"", "", "", "", "", "", "", "", ""});
            }
        });

        // Menambahkan action listener untuk tombol delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(Tabel_Database_Pasien_RS.this, "Pilih baris yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
    }

    // Fungsi untuk memuat data tabel dari file CSV
    private void loadTableData() throws IOException {
        File file = new File(patientDataFilePath);
        if (file.exists() && file.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                tableModel.setRowCount(0); // Mengosongkan data yang ada
                tableModel.setColumnCount(0); // Mengosongkan kolom yang ada
                boolean isFirstRow = true;

                while ((line = br.readLine()) != null) {
                    if (isFirstRow) {
                        addColumns(line);
                        isFirstRow = false;
                    } else {
                        addRow(line);
                    }
                }
            }
        } else {
            throw new FileNotFoundException("File tidak ada");
        }
    }

    // Fungsi untuk menambahkan kolom ke tabel
    private void addColumns(String line) {
        StringBuilder column = new StringBuilder();
        for (char ch : line.toCharArray()) {
            if (ch == ',') {
                tableModel.addColumn(column.toString().trim().replace("\"", ""));
                column.setLength(0);
            } else {
                column.append(ch);
            }
        }
        tableModel.addColumn(column.toString().trim().replace("\"", "")); // Tambahkan kolom terakhir
    }

    // Fungsi untuk menambahkan baris ke tabel
    private void addRow(String line) {
        StringBuilder field = new StringBuilder();
        Object[] data = new Object[tableModel.getColumnCount()];
        int columnIndex = 0;
        for (char ch : line.toCharArray()) {
            if (ch == ',') {
                data[columnIndex++] = field.toString().trim().replace("\"", "");
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }
        data[columnIndex] = field.toString().trim().replace("\"", ""); // Tambahkan field terakhir
        tableModel.addRow(data);
    }

    // Fungsi untuk menyimpan data tabel ke file CSV
    private void saveTableData() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(patientDataFilePath))) {
            // Menulis header
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                bw.write(tableModel.getColumnName(i));
                if (i < tableModel.getColumnCount() - 1) {
                    bw.write(",");
                }
            }
            bw.newLine();

            // Menulis data
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    bw.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Tabel_Database_Pasien_RS().setVisible(true);
            }
        });
    }
}
