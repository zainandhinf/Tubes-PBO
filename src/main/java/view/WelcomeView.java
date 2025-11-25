package view;

import javax.swing.*;
import java.awt.*;
import state.MesinATM;

/**
 * Kelas WelcomeView
 * Panel GUI yang muncul saat StateSiaga (Layar Awal).
 * Menampilkan pesan selamat datang dan kolom input nomor kartu (Read Only).
 */
public class WelcomeView extends JPanel {
    
    private JTextField cardField; // Field Tampilan Input

    public WelcomeView(MesinATM mesin) {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40)); 

        // --- BAGIAN TENGAH: TEKS SAMBUTAN ---
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setBackground(new Color(25, 30, 40));
        
        JLabel lblWelcome = new JLabel("SELAMAT DATANG", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 28));
        lblWelcome.setForeground(Color.WHITE);
        
        JLabel lblBank = new JLabel("DI BANK POLBAN", SwingConstants.CENTER);
        lblBank.setFont(new Font("Arial", Font.BOLD, 22));
        lblBank.setForeground(new Color(0, 200, 255));

        centerPanel.add(lblWelcome);
        centerPanel.add(lblBank);
        
        // --- BAGIAN BAWAH: KOLOM INPUT KARTU ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(25, 30, 40));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        JLabel lblInstruksi = new JLabel("Masukkan Nomor Kartu:", SwingConstants.CENTER);
        lblInstruksi.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInstruksi.setForeground(Color.GRAY);
        lblInstruksi.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Field Tampilan (Read Only - Diupdate dari MainFrame)
        cardField = new JTextField();
        cardField.setFont(new Font("Monospaced", Font.BOLD, 24));
        cardField.setHorizontalAlignment(JTextField.CENTER);
        cardField.setEditable(false); 
        cardField.setBackground(new Color(40, 45, 55));
        cardField.setForeground(new Color(0, 255, 150)); // Warna Hijau Neon
        cardField.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));

        bottomPanel.add(lblInstruksi, BorderLayout.NORTH);
        bottomPanel.add(cardField, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method ini dipanggil MainFrame saat tombol kanan ditekan
    public void updateCardDisplay(String text) {
        cardField.setText(text);
    }
}