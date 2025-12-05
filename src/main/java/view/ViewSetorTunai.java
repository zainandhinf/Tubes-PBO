package view;

import javax.swing.*;

import exception.GagalLoginException;
import exception.InputTidakValidException;

import java.awt.*;
import state.MesinATM;
import util.UIConstants;

/**
 * Kelas ViewSetorTunai
 * Layar untuk menampilkan form input setor tunai
 */
public class ViewSetorTunai extends JPanel {
    
    /**
     * Instans MesinATM yang digunakan oleh view ini.
     * Ditandai sebagai transient karena komponen Swing dapat diserialisasi,
     * sedangkan MesinATM tidak perlu dan tidak aman untuk ikut diserialisasi.
     */
    private transient MesinATM mesin;
    
    private JLabel nominalLabel;
    private JLabel statusLabel;
    private JLabel inputPromptLabel;
    private JLabel saldoSekarangLabel;
    
    // State untuk tracking input
    @SuppressWarnings("unused")
    private String nominal = "";
    
    public ViewSetorTunai(MesinATM mesin) {
        this.mesin = mesin;
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40));
        initComponents();
    }
    
    private void initComponents() {
        // Header
        JLabel lblHeader = new JLabel("SETOR TUNAI", SwingConstants.CENTER);
        lblHeader.setFont(new Font(UIConstants.FONT_ARIAL, Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 200, 255));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);
        
        // Content Panel 
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(40, 45, 55));
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Saldo Sekarang 
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 30, 10, 15);
        JLabel saldoLabel = new JLabel("Saldo Sekarang:");
        saldoLabel.setFont(new Font(UIConstants.FONT_MONO, Font.PLAIN, 14));
        saldoLabel.setForeground(Color.WHITE);
        contentPanel.add(saldoLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.insets = new Insets(30, 15, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        saldoSekarangLabel = new JLabel("Rp 0");
        saldoSekarangLabel.setFont(new Font(UIConstants.FONT_MONO, Font.BOLD, 14));
        saldoSekarangLabel.setForeground(new Color(0, 255, 150)); // Hijau Neon
        contentPanel.add(saldoSekarangLabel, gbc);
        
        // Nominal Setor
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.insets = new Insets(10, 30, 10, 15);
        gbc.fill = GridBagConstraints.NONE;
        JLabel nomLabel = new JLabel("Nominal Setor:");
        nomLabel.setFont(new Font(UIConstants.FONT_MONO, Font.PLAIN, 14));
        nomLabel.setForeground(Color.WHITE);
        contentPanel.add(nomLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(10, 15, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nominalLabel = new JLabel(UIConstants.PLACEHOLDER_RP);
        nominalLabel.setFont(new Font(UIConstants.FONT_MONO, Font.BOLD, 14));
        nominalLabel.setForeground(new Color(0, 255, 150)); // Hijau Neon
        contentPanel.add(nominalLabel, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        statusLabel = new JLabel("Status: Siap untuk setor tunai");
        statusLabel.setFont(new Font(UIConstants.FONT_MONO, Font.PLAIN, 12));
        statusLabel.setForeground(Color.CYAN);
        contentPanel.add(statusLabel, gbc);
        
        // Input prompt
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 30, 30, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPromptLabel = new JLabel("<html>Masukkan nominal setor tunai<br/>(tanpa titik/koma):</html>");
        inputPromptLabel.setFont(new Font(UIConstants.FONT_MONO, Font.BOLD, 12));
        inputPromptLabel.setForeground(new Color(0, 200, 255));
        contentPanel.add(inputPromptLabel, gbc);
        
        // Padding kiri kanan agar tidak mepet (seperti ViewRiwayat)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(25, 30, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        // Footer Instruksi
        JLabel lblFooter = new JLabel("Tekan ENTER untuk konfirmasi | CANCEL untuk kembali", SwingConstants.CENTER);
        lblFooter.setFont(new Font(UIConstants.FONT_ARIAL, Font.ITALIC, 14));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }
    
    public void updateInput(String input) {
        nominal = input;
        if (input.isEmpty()) {
            nominalLabel.setText(UIConstants.PLACEHOLDER_RP);
        } else {
            nominalLabel.setText("Rp " + formatRupiah(input));
        }
    }
    
    public void handleInput(String input) {
        try {
            if (input.trim().isEmpty()) {
                throw new IllegalArgumentException("Nominal tidak boleh kosong!");
            }
            
            // Parse nominal dengan support format ribuan
            String cleanInput = input.replace(".", "").replace(",", "");
            double nominalValue = Double.parseDouble(cleanInput);
            
            if (nominalValue <= 0) {
                throw new IllegalArgumentException("Nominal harus lebih besar dari 0");
            }
            
            // Proses setor tunai
            prosesSetorTunai(nominalValue);

            statusLabel.setText("Status: Setor tunai berhasil!");
            statusLabel.setForeground(Color.CYAN);
            inputPromptLabel.setText("<html>Setor tunai selesai.<br/>Tekan CANCEL untuk kembali.</html>");
            
            // Update saldo sekarang
            updateSaldoSekarang();
            
            // Kembali ke menu setelah 5 detik
            Timer timer = new Timer(5000, e -> {
                mesin.ubahState(new state.StateMenuUtama());
                ((MainFrame) SwingUtilities.getWindowAncestor(this)).gantiLayar("MENU");
            });
            timer.setRepeats(false);
            timer.start();
            
        } catch (NumberFormatException e) {
        statusLabel.setText("Error: Input tidak valid, hanya angka yang diperbolehkan.");
        statusLabel.setForeground(Color.RED);

        } catch (IllegalArgumentException e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }

    private void prosesSetorTunai(double nominalValue) {
    try {
        mesin.getProxy().setorTunai(nominalValue);
        JOptionPane.showMessageDialog(this, "Setor Tunai Berhasil!");
    } catch (InputTidakValidException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Input Salah", JOptionPane.ERROR_MESSAGE);
    } catch (GagalLoginException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
    }
}

    
    public void resetForm() {
        nominal = "";
        nominalLabel.setText(UIConstants.PLACEHOLDER_RP);
        inputPromptLabel.setText("<html>Masukkan nominal setor tunai<br/>(tanpa titik/koma):</html>");
        statusLabel.setText("Status: Siap untuk setor tunai");
        statusLabel.setForeground(Color.CYAN);
        updateSaldoSekarang();
    }
    
    private void updateSaldoSekarang() {
        try {
            double saldo = mesin.getProxy().cekSaldo();
            saldoSekarangLabel.setText(String.format("Rp %,.0f", saldo));
        } catch (RuntimeException  e) {
            saldoSekarangLabel.setText("Rp 0");
        }
    }
    
    private String formatRupiah(String input) {
        try {
            String cleanInput = input.replace(".", "").replace(",", "");
            double value = Double.parseDouble(cleanInput);
            return String.format("%,.0f", value);
        } catch (NumberFormatException e) {
            return input;
        }
    }
}