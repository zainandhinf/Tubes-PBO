package view;

import javax.swing.*;
import java.awt.*;
import state.MesinATM;

/**
 * Kelas ViewMenu
 * Menampilkan daftar menu utama transaksi yang tersedia.
 * User memilih menu dengan mengklik tombol.
 */
public class ViewMenu extends JPanel {
    
    public ViewMenu(MesinATM mesin) {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 30, 40));

        // 1. Header: "Halo, [Nomor Rekening]"
        JLabel lblHeader = new JLabel("MENU TRANSAKSI", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(new Color(0, 200, 255));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 2. Grid Menu (3 Baris, 2 Kolom)
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        menuPanel.setBackground(new Color(25, 30, 40));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));

        // Daftar Tombol Menu
        // Format: { "Label Tombol", "Kode Menu (int)" }
        String[][] menuItems = {
            {"1. CEK SALDO", "1"},
            {"2. TARIK TUNAI", "2"},
            {"3. SETOR TUNAI", "3"},
            {"4. TRANSFER", "4"},
            {"5. RIWAYAT", "5"},
            {"6. KELUAR", "6"}
        };

        for (String[] item : menuItems) {
            JButton btn = createMenuButton(item[0]);
            int kodeMenu = Integer.parseInt(item[1]);
            
            // Aksi Tombol: Panggil pilihMenu di Controller
            btn.addActionListener(e -> {
                System.out.println("[GUI] Memilih Menu: " + item[0]);
                mesin.pilihMenu(kodeMenu);
            });
            
            menuPanel.add(btn);
        }

        add(menuPanel, BorderLayout.CENTER);
    }

    /**
     * Helper untuk membuat tombol menu yang seragam
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(new Color(60, 70, 90));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efek Hover Sederhana
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(80, 90, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(60, 70, 90));
            }
        });
        
        return btn;
    }
}