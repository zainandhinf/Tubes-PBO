package view;

import javax.swing.*;
import java.awt.*;
import state.MesinATM;

/**
 * Kelas ViewTransfer
 * Layar untuk menampilkan form input transfer antar rekening
 */
public class ViewTransfer extends JPanel {
    
    private MesinATM mesin;
    private JLabel rekTujuanLabel;
    private JLabel nominalLabel;
    private JLabel statusLabel;
    private JLabel inputPromptLabel;
    
    // State untuk tracking input
    private boolean isInputtingRekening = true;
    private String rekeningTujuan = "";
    private String nominal = "";
    
    public ViewTransfer(MesinATM mesin) {
        this.mesin = mesin;
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40));
        initComponents();
    }
    
    private void initComponents() {
        // 1. Header
        JLabel lblHeader = new JLabel("TRANSFER ANTAR REKENING", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 200, 255));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);
        
        // 2. Content Panel 
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(40, 45, 55));
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Rekening Tujuan
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 30, 10, 15);
        JLabel rekLabel = new JLabel("Rekening Tujuan:");
        rekLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        rekLabel.setForeground(Color.WHITE);
        contentPanel.add(rekLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.insets = new Insets(30, 15, 10, 30);
        rekTujuanLabel = new JLabel("____________");
        rekTujuanLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        rekTujuanLabel.setForeground(new Color(0, 255, 150)); // Hijau Neon 
        contentPanel.add(rekTujuanLabel, gbc);
        
        // Nominal
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.insets = new Insets(10, 30, 10, 15);
        JLabel nomLabel = new JLabel("Nominal (Rp):");
        nomLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        nomLabel.setForeground(Color.WHITE);
        contentPanel.add(nomLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(10, 15, 10, 30);
        nominalLabel = new JLabel("____________");
        nominalLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        nominalLabel.setForeground(new Color(0, 255, 150)); // Hijau Neon
        contentPanel.add(nominalLabel, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 30, 20, 30);
        statusLabel = new JLabel("Status: Siap untuk transfer");
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statusLabel.setForeground(Color.CYAN);
        contentPanel.add(statusLabel, gbc);
        
        // Input prompt
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 30, 30, 30);
        inputPromptLabel = new JLabel("Masukkan nomor rekening tujuan:");
        inputPromptLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        inputPromptLabel.setForeground(new Color(0, 200, 255));
        contentPanel.add(inputPromptLabel, gbc);
        
        // Padding kiri kanan agar tidak mepet 
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(25, 30, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        // 3. Footer Instruksi 
        JLabel lblFooter = new JLabel("Tekan ENTER untuk konfirmasi | CANCEL untuk kembali", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 14));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }
    
    public void updateInput(String input) {
        if (isInputtingRekening) {
            rekeningTujuan = input;
            rekTujuanLabel.setText(input.isEmpty() ? "____________" : input);
        } else {
            nominal = input;
            nominalLabel.setText(input.isEmpty() ? "____________" : formatRupiah(input));
        }
    }
    
    public void handleInput(String input) {
        try {
            if (isInputtingRekening) {
                if (input.trim().isEmpty()) {
                    throw new Exception("Nomor rekening tidak boleh kosong!");
                }
                
                rekeningTujuan = input;
                rekTujuanLabel.setText(rekeningTujuan);
                isInputtingRekening = false;
                inputPromptLabel.setText("Masukkan nominal transfer (tanpa titik/koma):");
                statusLabel.setText("Status: Menunggu input nominal...");
                statusLabel.setForeground(Color.ORANGE);
                
            } else {
                if (input.trim().isEmpty()) {
                    throw new Exception("Nominal tidak boleh kosong!");
                }
                
                // Parse nominal dengan support format ribuan
                String cleanInput = input.replace(".", "").replace(",", "");
                double nominalValue = Double.parseDouble(cleanInput);
                
                if (nominalValue <= 0) {
                    throw new Exception("Nominal harus lebih besar dari 0");
                }
                
                // Proses transfer
                mesin.getProxy().transfer(nominalValue, rekeningTujuan);
                
                statusLabel.setText("Status: Transfer berhasil!");
                statusLabel.setForeground(Color.CYAN);
                inputPromptLabel.setText("Transfer selesai. Tekan CANCEL untuk kembali ke menu.");
                
                // Kembali ke menu setelah 5 detik
                Timer timer = new Timer(5000, e -> {
                    mesin.ubahState(new state.StateMenuUtama());
                    ((MainFrame) SwingUtilities.getWindowAncestor(this)).gantiLayar("MENU");
                });
                timer.setRepeats(false);
                timer.start();
                
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }
    
    public void resetForm() {
        isInputtingRekening = true;
        rekeningTujuan = "";
        nominal = "";
        rekTujuanLabel.setText("____________");
        nominalLabel.setText("____________");
        inputPromptLabel.setText("Masukkan nomor rekening tujuan:");
        statusLabel.setText("Status: Siap untuk transfer");
        statusLabel.setForeground(Color.CYAN);
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