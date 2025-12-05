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
    
    /**
     * Instans MesinATM yang digunakan oleh view ini.
     * Ditandai sebagai transient karena komponen Swing dapat diserialisasi,
     * sedangkan MesinATM tidak perlu dan tidak aman untuk ikut diserialisasi.
     */
    @SuppressWarnings("unused")
    private transient MesinATM mesin;

    private JPasswordField pinField;

    public LoginView(MesinATM mesin) {
        this.mesin = mesin;
        setLayout(new GridBagLayout());
        setBackground(new Color(25, 30, 40));

        JPanel centerBox = new JPanel(new GridLayout(2, 1, 10, 10));
        centerBox.setBackground(new Color(25, 30, 40));
        centerBox.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));
        
        JLabel lblPin = new JLabel("MASUKKAN PIN:", SwingConstants.CENTER);
        lblPin.setForeground(new Color(0, 200, 255));
        lblPin.setFont(new Font("Arial", Font.BOLD, 24));
        
        pinField = new JPasswordField(6);
        pinField.setFont(new Font("Arial", Font.BOLD, 32));
        pinField.setHorizontalAlignment(SwingConstants.CENTER);
        pinField.setEditable(false);
        pinField.setBackground(Color.WHITE);
        
        centerBox.add(lblPin);
        centerBox.add(pinField);
        
        add(centerBox);
    }

    // Method ini dipanggil oleh MainFrame saat tombol kanan ditekan
    public void updatePinDisplay(String text) {
        pinField.setText(text);
    }
}