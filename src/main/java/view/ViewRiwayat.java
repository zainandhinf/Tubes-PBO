package view;

import javax.swing.*;

import state.MesinATM;

import java.awt.*;
import java.util.List;

/**
 * Kelas ViewRiwayat
 * Menampilkan layar riwayat transaksi.
 */
public class ViewRiwayat extends JPanel {
    @SuppressWarnings("unused")
    private transient MesinATM mesin;
    
    private JTextArea textInfo; // Area untuk menampilkan teks riwayat

    public ViewRiwayat(MesinATM mesin) {
        this.mesin = mesin;
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40));

        // 1. Header
        JLabel lblHeader = new JLabel("MUTASI REKENING", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 200, 255));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 2. Area Teks Riwayat
        textInfo = new JTextArea();
        textInfo.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textInfo.setEditable(false);
        textInfo.setBackground(new Color(40, 45, 55));
        textInfo.setForeground(new Color(0, 255, 150)); // Hijau Neon
        textInfo.setMargin(new Insets(10, 10, 10, 10));

        // Bungkus dengan ScrollPane
        JScrollPane scrollPane = new JScrollPane(textInfo);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));
        scrollPane.getViewport().setBackground(new Color(40, 45, 55));
        
        // Padding kiri kanan agar tidak mepet
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(25, 30, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        // 3. Footer Instruksi
        JLabel lblFooter = new JLabel("Tekan CANCEL untuk Kembali", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 14));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    /**
     * Method untuk mengisi data riwayat ke layar.
     * Dipanggil saat layar ini dibuka.
     */
    public void updateData(List<String> listRiwayat) {
        textInfo.setText(""); // Bersihkan dulu
        
        if (listRiwayat == null || listRiwayat.isEmpty()) {
            textInfo.setText("\n  Belum ada transaksi.");
        } else {
            for (String log : listRiwayat) {
                textInfo.append(log + "\n----------------------------------------\n");
            }
        }
        // Scroll ke paling atas
        textInfo.setCaretPosition(0);
    }
}