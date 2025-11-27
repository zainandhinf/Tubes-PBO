package view;

import javax.swing.*;
import java.awt.*;
import state.MesinATM;

/**
 * Kelas MainFrame
 * Merupakan Container utama (JFrame) yang menampung seluruh tampilan aplikasi.
 * Menggunakan layout terpisah (Split Layout):
 * - Kiri: Layar Monitor (Dinamis, berubah sesuai State)
 * - Kanan: Panel Kontrol (Statis, berisi Keypad & Tombol Aksi)
 */
public class MainFrame extends JFrame {
    
    private MesinATM mesin;
    
    // Panel Kiri (Layar Monitor)
    private JPanel screenContainer;
    private CardLayout cardLayout;
    
    // Variabel Penyimpan Ketikan (Pengganti InputField Kanan)
    private String currentInputBuffer = ""; 

    public MainFrame(MesinATM mesin) {
        this.mesin = mesin;
        
        setTitle("SIMULASI ATM BANK POLBAN");
        setSize(1000, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2)); 

        // ============================================================
        // 1. BAGIAN KIRI: LAYAR MONITOR (SCREEN)
        // ============================================================
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(25, 30, 40)); 
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel screenBezel = new JPanel(new BorderLayout());
        screenBezel.setBackground(new Color(40, 45, 55)); 
        screenBezel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 70, 85), 10), 
            BorderFactory.createLineBorder(new Color(0, 200, 255), 2)  
        ));

        cardLayout = new CardLayout();
        screenContainer = new JPanel(cardLayout);
        screenContainer.setBackground(Color.BLACK); 

        // --- DAFTARKAN VIEW DI SINI ---
        screenContainer.add(new WelcomeView(mesin), "WELCOME");
        screenContainer.add(new LoginView(mesin), "LOGIN");
        screenContainer.add(new ViewMenu(mesin), "MENU");
        
        screenBezel.add(screenContainer, BorderLayout.CENTER);
        
        JLabel bankLabel = new JLabel("BANK POLBAN", SwingConstants.CENTER);
        bankLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bankLabel.setForeground(new Color(0, 200, 255));
        bankLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        leftPanel.add(bankLabel, BorderLayout.NORTH);
        leftPanel.add(screenBezel, BorderLayout.CENTER);

        // ============================================================
        // 2. BAGIAN KANAN: TOMBOL & KEYPAD (CONTROLS)
        // ============================================================
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(35, 40, 50)); 
        rightPanel.setBorder(BorderFactory.createEmptyBorder(80, 30, 80, 30));

        // -- HAPUS INPUT FIELD DI SINI (Sudah dipindah ke View Kiri) --

        JPanel keypadContainer = new JPanel(new BorderLayout(10, 0));
        keypadContainer.setBackground(new Color(35, 40, 50));

        JPanel numpadPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        numpadPanel.setBackground(new Color(35, 40, 50));
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "CLEAR", "0", "⌫"};
        
        for (String k : keys) {
            JButton btn = createButton(k, new Color(50, 60, 75));
            if (k.equals("CLEAR")) {
                btn.setBackground(new Color(220, 150, 0)); 
                btn.addActionListener(e -> {
                    currentInputBuffer = ""; 
                    updateActiveView();      
                });
            } else if (k.equals("⌫")) {
                btn.setBackground(new Color(150, 50, 50)); 
                btn.addActionListener(e -> {
                    if(currentInputBuffer.length() > 0) {
                        currentInputBuffer = currentInputBuffer.substring(0, currentInputBuffer.length()-1);
                        updateActiveView();
                    }
                });
            } else {
                btn.addActionListener(e -> {
                    currentInputBuffer += k; 
                    updateActiveView();      
                });
            }
            numpadPanel.add(btn);
        }

        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        actionPanel.setBackground(new Color(35, 40, 50));
        actionPanel.setPreferredSize(new Dimension(100, 0));

        JButton btnEnter = createButton("ENTER", new Color(0, 180, 80)); 
        JButton btnCancel = createButton("CANCEL", new Color(200, 50, 50)); 
        JButton btnExit = createButton("EXIT", Color.GRAY);

        btnEnter.addActionListener(e -> handleEnterPressed());
        btnCancel.addActionListener(e -> {
            currentInputBuffer = "";
            updateActiveView();
        });
        btnExit.addActionListener(e -> System.exit(0));

        actionPanel.add(btnEnter);
        actionPanel.add(btnCancel);
        actionPanel.add(btnExit);

        keypadContainer.add(numpadPanel, BorderLayout.CENTER);
        keypadContainer.add(actionPanel, BorderLayout.EAST);

        rightPanel.add(keypadContainer, BorderLayout.CENTER);

        add(leftPanel);
        add(rightPanel);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(bg.darker(), 2));
        return btn;
    }

    public void gantiLayar(String namaLayar) {
        cardLayout.show(screenContainer, namaLayar);
        currentInputBuffer = ""; 
        updateActiveView();      
    }

    private void handleEnterPressed() {
        String input = currentInputBuffer;
        System.out.println("[DEBUG] Input dikirim: " + input);

        if (mesin.getStateSaatIni() instanceof state.StateSiaga) {
            mesin.masukkanKartu(input);
        } else if (mesin.getStateSaatIni() instanceof state.StateCekPin) {
            mesin.masukkanPin(input);
        } else if (mesin.getStateSaatIni() instanceof state.StateMenuUtama) {
            try {
                int pilihan = Integer.parseInt(input);
                mesin.pilihMenu(pilihan);
            } catch (NumberFormatException e) {}
        }
        
        currentInputBuffer = "";
        updateActiveView();
    }

    private void updateActiveView() {
        for (Component comp : screenContainer.getComponents()) {
            if (comp.isVisible()) {
                if (comp instanceof WelcomeView) {
                    ((WelcomeView) comp).updateCardDisplay(currentInputBuffer);
                } else if (comp instanceof LoginView) {
                    ((LoginView) comp).updatePinDisplay(currentInputBuffer);
                } else if (comp instanceof ViewMenu) {
                    ((ViewMenu) comp).updateMenuInput(currentInputBuffer);
                }
                // Nanti tambahkan: else if (comp instanceof WithdrawView) ...
            }
        }
    }
}