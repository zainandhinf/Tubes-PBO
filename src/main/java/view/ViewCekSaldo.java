package view;

import javax.swing.*;
import java.awt.*;
import state.MesinATM;

/**
 * Kelas ViewCekSaldo
 * Menampilkan informasi saldo nasabah.
 */
public class ViewCekSaldo extends JPanel {

    private JLabel lblSaldo;

    public ViewCekSaldo(MesinATM mesin) {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40));

        // 1. Header
        JLabel lblHeader = new JLabel("INFORMASI SALDO", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 200, 255));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 2. Content (Saldo)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(25, 30, 40));

        JLabel lblLabel = new JLabel("Saldo Rekening Anda:");
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        lblLabel.setForeground(Color.WHITE);

        lblSaldo = new JLabel("Rp 0");
        lblSaldo.setFont(new Font("Monospaced", Font.BOLD, 36));
        lblSaldo.setForeground(new Color(0, 255, 150));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        contentPanel.add(lblLabel, gbc);

        gbc.gridy = 1;
        contentPanel.add(lblSaldo, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // 3. Footer
        JLabel lblFooter = new JLabel("Tekan CANCEL untuk kembali ke Menu Utama", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 14));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    public void updateSaldo(double saldo) {
        lblSaldo.setText(String.format("Rp %,.0f", saldo));
    }
}
