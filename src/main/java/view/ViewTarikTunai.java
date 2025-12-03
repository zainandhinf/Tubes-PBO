package view;

import javax.swing.*;
import java.awt.*;
import state.MesinATM;

/**
 * Kelas ViewTarikTunai
 * Menampilkan layar input nominal penarikan.
 */
public class ViewTarikTunai extends JPanel {

    private JTextField inputField;

    public ViewTarikTunai(MesinATM mesin) {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40));

        // 1. Header
        JLabel lblHeader = new JLabel("TARIK TUNAI", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 200, 255));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 2. Content (Input Nominal)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(25, 30, 40));

        JLabel lblLabel = new JLabel("Masukkan Nominal Penarikan:");
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        lblLabel.setForeground(Color.WHITE);

        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.BOLD, 36));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setEditable(false); // Input dari keypad
        inputField.setBackground(new Color(25, 30, 40));
        inputField.setForeground(new Color(0, 255, 150));
        inputField.setBorder(null);
        inputField.setPreferredSize(new Dimension(400, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 20, 10);
        contentPanel.add(lblLabel, gbc);

        gbc.gridy = 1;
        contentPanel.add(inputField, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // 3. Footer
        JLabel lblFooter = new JLabel("Tekan ENTER untuk Lanjut, CANCEL untuk Batal", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 14));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    public void updateInput(String text) {
        // Format angka dengan pemisah ribuan
        try {
            if (text.isEmpty()) {
                inputField.setText("");
            } else {
                long val = Long.parseLong(text);
                inputField.setText(String.format("%,d", val));
            }
        } catch (NumberFormatException e) {
            inputField.setText(text);
        }
    }
}
