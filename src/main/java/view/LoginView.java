package view;

import state.MesinATM;

import javax.swing.*;
import java.awt.*;

/**
 * Kelas LoginView
 * Tampilan GUI untuk memasukkan PIN.
 * Menggunakan JPanel agar bisa dimasukkan ke dalam MainFrame (CardLayout).
 */
public class LoginView extends JPanel {
    private MesinATM mesin;
    private JPasswordField pinField;

    public LoginView(MesinATM mesin) {
        this.mesin = mesin;
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40)); // Warna Tema Gelap

        // 1. Panel Atas (Layar PIN)
        JPanel screenPanel = new JPanel(new FlowLayout());
        screenPanel.setBackground(new Color(25, 30, 40));
        
        JLabel lblPin = new JLabel("MASUKKAN PIN: ");
        lblPin.setForeground(new Color(0, 200, 255));
        lblPin.setFont(new Font("Arial", Font.BOLD, 20));
        
        pinField = new JPasswordField(6);
        pinField.setFont(new Font("Arial", Font.BOLD, 24));
        pinField.setHorizontalAlignment(JPasswordField.CENTER);
        pinField.setEditable(false); // Tidak bisa ketik keyboard laptop (harus klik tombol)

        screenPanel.add(lblPin);
        screenPanel.add(pinField);
        add(screenPanel, BorderLayout.NORTH);

        // 2. Panel Tengah (Tombol Angka Numpad)
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        keypadPanel.setBackground(new Color(25, 30, 40));
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "CLEAR", "0", "ENTER"};

        for (String k : keys) {
            JButton btn = createKeypadButton(k);
            
            // Logika Tombol
            if (k.equals("CLEAR")) {
                btn.setBackground(new Color(200, 50, 50)); // Merah
                btn.addActionListener(e -> pinField.setText(""));
            } else if (k.equals("ENTER")) {
                btn.setBackground(new Color(0, 180, 80)); // Hijau
                btn.addActionListener(e -> prosesLogin());
            } else {
                // Tombol Angka
                btn.addActionListener(e -> {
                    String current = new String(pinField.getPassword());
                    if (current.length() < 6) {
                        pinField.setText(current + k);
                    }
                });
            }
            keypadPanel.add(btn);
        }
        add(keypadPanel, BorderLayout.CENTER);
    }

    /**
     * Helper untuk membuat tombol agar seragam
     */
    private JButton createKeypadButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(new Color(50, 60, 75));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Mengirim input PIN ke State Controller
     */
    private void prosesLogin() {
        String inputPin = new String(pinField.getPassword());
        if (inputPin.length() == 6) {
            // Delegasi ke State (Controller)
            mesin.getStateSaatIni().masukkanPin(mesin, inputPin);
            pinField.setText(""); // Reset field setelah submit
        } else {
            JOptionPane.showMessageDialog(this, "PIN harus 6 digit!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}