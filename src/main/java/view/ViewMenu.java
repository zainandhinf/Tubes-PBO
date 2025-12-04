package view;

import javax.swing.*;

import state.MesinATM;

import java.awt.*;
import util.UIConstants;

/**
 * Kelas ViewMenu
 * Menampilkan daftar menu secara visual (Statis).
 * User memilih menu dengan menekan angka di keypad kanan.
 */
public class ViewMenu extends JPanel {
    @SuppressWarnings("unused")
    private transient MesinATM mesin;
    
    private JTextField menuInputField; // Field untuk menampilkan input pilihan menu

    public ViewMenu(MesinATM mesin) {
        this.mesin = mesin;
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40));

        // 1. Header
        JLabel lblHeader = new JLabel("SILAKAN PILIH TRANSAKSI", SwingConstants.CENTER);
        lblHeader.setFont(new Font(UIConstants.FONT_ARIAL, Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 200, 255));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 2. Grid Menu (Tampilan Label Saja)
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        menuPanel.setBackground(new Color(25, 30, 40));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Daftar Menu
        String[] menuItems = {
            "1. CEK SALDO", 
            "2. TARIK TUNAI",
            "3. SETOR TUNAI", 
            "4. TRANSFER",
            "5. RIWAYAT",     
            "6. KELUAR"
        };

        for (String text : menuItems) {
            JPanel itemPanel = createMenuItem(text);
            menuPanel.add(itemPanel);
        }

        add(menuPanel, BorderLayout.CENTER);
        
        // 3. Footer dengan Input Display
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(25, 30, 40));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 30, 100)); // Padding kiri kanan besar biar input di tengah

        JLabel lblInfo = new JLabel("Pilihan Menu: ", SwingConstants.CENTER);
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setFont(new Font(UIConstants.FONT_ARIAL, Font.PLAIN, 14));
        
        menuInputField = new JTextField();
        menuInputField.setFont(new Font(UIConstants.FONT_MONO, Font.BOLD, 24));
        menuInputField.setHorizontalAlignment(SwingConstants.CENTER);
        menuInputField.setEditable(false);
        menuInputField.setBackground(new Color(40, 45, 55));
        menuInputField.setForeground(new Color(0, 255, 150));
        menuInputField.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));

        bottomPanel.add(lblInfo, BorderLayout.NORTH);
        bottomPanel.add(menuInputField, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Helper membuat tampilan menu (Kotak Label Statis)
     */
    private JPanel createMenuItem(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(40, 45, 55)); 
        p.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));
        
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font(UIConstants.FONT_MONO, Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);
        
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    /**
     * Method ini dipanggil oleh MainFrame saat tombol angka kanan ditekan
     */
    public void updateMenuInput(String text) {
        menuInputField.setText(text);
    }
}