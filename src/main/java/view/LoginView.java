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
        setLayout(new GridBagLayout());
        setBackground(new Color(25, 30, 40)); // Warna Tema Gelap

        JPanel centerBox = new JPanel(new GridLayout(2, 1, 10, 10));
        centerBox.setBackground(new Color(25, 30, 40));
        centerBox.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));
        
        JLabel lblPin = new JLabel("MASUKKAN PIN:", SwingConstants.CENTER);
        lblPin.setForeground(new Color(0, 200, 255));
        lblPin.setFont(new Font("Arial", Font.BOLD, 24));
        
        pinField = new JPasswordField(6);
        pinField.setFont(new Font("Arial", Font.BOLD, 32));
        pinField.setHorizontalAlignment(JPasswordField.CENTER);
        pinField.setEditable(false); // Tidak bisa diketik langsung
        pinField.setBackground(Color.WHITE);
        
        // HAPUS SEMUA KODE PANEL KEYPAD DI BAWAH SINI
        
        centerBox.add(lblPin);
        centerBox.add(pinField);
        
        add(centerBox);
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

    // Method ini dipanggil oleh MainFrame saat tombol kanan ditekan
    public void updatePinDisplay(String text) {
        pinField.setText(text);
    }
}