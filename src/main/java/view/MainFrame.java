package view;

import javax.swing.*;
import java.awt.*;
import state.MesinATM;
import state.StateTransfer;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Kelas MainFrame
 * Merupakan Container utama (JFrame) yang menampung seluruh tampilan aplikasi.
 * Menggunakan layout terpisah (Split Layout):
 * - Kiri: Layar Monitor (Dinamis, berubah sesuai State)
 * - Kanan: Panel Kontrol (Statis, berisi Keypad & Tombol Aksi)
 */
public class MainFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(MainFrame.class.getName());

    /**
     * Instans MesinATM yang digunakan oleh view ini.
     * Ditandai sebagai transient karena komponen Swing dapat diserialisasi,
     * sedangkan MesinATM tidak perlu dan tidak aman untuk ikut diserialisasi.
     */
    private transient MesinATM mesin;

    // Panel Kiri (Layar Monitor)
    private JPanel screenContainer;
    private CardLayout cardLayout;

    // Variabel Penyimpan Ketikan (Pengganti InputField Kanan)
    private String currentInputBuffer = "";
    
    // Transfer view components
    private ViewTransfer transferView;
    
    // Setor tunai view components
    private ViewSetorTunai setorTunaiView;

    // Helper untuk cek apakah state saat ini adalah salah satu state yang kembali ke menu utama
    private boolean isReturnToMenuState(state.StateATM s) {
        return s instanceof state.StateRiwayat ||
            s instanceof StateTransfer ||
            s instanceof state.StateSetorTunai ||
            s instanceof state.StateCekSaldo ||
            s instanceof state.StateTarikTunai;
    }

    private final StringBuilder inputBuffer = new StringBuilder();

    public MainFrame(MesinATM mesin) {
        this.mesin = mesin;

        setTitle("SIMULASI ATM BANK POLBAN");
        setSize(1000, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
                BorderFactory.createLineBorder(new Color(0, 200, 255), 2)));

        cardLayout = new CardLayout();
        screenContainer = new JPanel(cardLayout);
        screenContainer.setBackground(Color.BLACK);

        // --- DAFTARKAN VIEW DI SINI ---
        screenContainer.add(new WelcomeView(mesin), "WELCOME");
        screenContainer.add(new LoginView(mesin), "LOGIN");
        screenContainer.add(new ViewMenu(mesin), "MENU");
        screenContainer.add(new ViewCekSaldo(mesin), "CEK_SALDO");
        screenContainer.add(new ViewTarikTunai(mesin), "TARIK_TUNAI");
        screenContainer.add(new ViewRiwayat(mesin), "RIWAYAT");
        
        transferView = new ViewTransfer(mesin);
        screenContainer.add(transferView, "TRANSFER");
        
        setorTunaiView = new ViewSetorTunai(mesin);
        screenContainer.add(setorTunaiView, "SETOR_TUNAI");

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

        JPanel keypadContainer = new JPanel(new BorderLayout(10, 0));
        keypadContainer.setBackground(new Color(35, 40, 50));

        JPanel numpadPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        numpadPanel.setBackground(new Color(35, 40, 50));
        String[] keys = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "CLEAR", "0", "<" };

        for (String k : keys) {
            JButton btn = createButton(k, new Color(50, 60, 75));
            if (k.equals("CLEAR")) {
                btn.setBackground(new Color(220, 150, 0));
                btn.addActionListener(e -> {
                    inputBuffer.setLength(0);
                    currentInputBuffer = "";
                    updateActiveView();
                });
            } else if (k.equals("<")) {
                btn.setBackground(new Color(150, 50, 50));
                btn.addActionListener(e -> {
                    if (!currentInputBuffer.isEmpty()) {
                        currentInputBuffer = currentInputBuffer.substring(0, currentInputBuffer.length() - 1);
                        updateActiveView();
                    }
                });
            } else {
                btn.addActionListener(e -> {
                    inputBuffer.append(k);
                    currentInputBuffer = inputBuffer.toString();
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
        btnCancel.addActionListener(e -> handleCancelPressed());
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
        inputBuffer.setLength(0);
        currentInputBuffer = "";

        switch (namaLayar) {
            case "RIWAYAT" -> updateRiwayatView();
            case "CEK_SALDO" -> updateCekSaldoView();
            case "TARIK_TUNAI" -> resetTarikTunaiView();
            case "TRANSFER" -> transferView.resetForm();
            case "SETOR_TUNAI" -> setorTunaiView.resetForm();
            default -> {
                // Tidak ada aksi khusus
            }
        }

        updateActiveView();
    }

    private void updateRiwayatView() {
        for (Component comp : screenContainer.getComponents()) {
            if (comp instanceof ViewRiwayat view) {
                try {
                    var logs = mesin.getProxy().getRiwayat();
                    view.updateData(logs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateCekSaldoView() {
        for (Component comp : screenContainer.getComponents()) {
            if (comp instanceof ViewCekSaldo view) {
                try {
                    double saldo = mesin.getProxy().cekSaldo();
                    view.updateSaldo(saldo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resetTarikTunaiView() {
        for (Component comp : screenContainer.getComponents()) {
            if (comp instanceof ViewTarikTunai view) {
                view.updateInput("");
            }
        }
    }


    private void handleEnterPressed() {
        String input = currentInputBuffer;
        LOGGER.log(Level.INFO, "[DEBUG] Input dikirim: {0}", input);

        if (mesin.getStateSaatIni() instanceof state.StateSiaga) {
            mesin.masukkanKartu(input);
        } else if (mesin.getStateSaatIni() instanceof state.StateCekPin) {
            mesin.masukkanPin(input);
        } else if (mesin.getStateSaatIni() instanceof state.StateMenuUtama) {
            try {
                int pilihan = Integer.parseInt(input);
                mesin.pilihMenu(pilihan);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Input tidak valid", e);
            }
        } else if (mesin.getStateSaatIni() instanceof state.StateTarikTunai) {
            try {
                // Hapus titik/koma jika ada
                double nominal = Double.parseDouble(input.replace(".", "").replace(",", ""));
                mesin.prosesJumlah(nominal);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "[ERROR] Input nominal tidak valid: {0}", input);
            }
        } else if (mesin.getStateSaatIni() instanceof StateTransfer) {
            transferView.handleInput(input);
        } else if (mesin.getStateSaatIni() instanceof state.StateSetorTunai) {
            setorTunaiView.handleInput(input);
        }

        inputBuffer.setLength(0);
        currentInputBuffer = "";
        updateActiveView();
    }

    private void updateActiveView() {
        for (Component comp : screenContainer.getComponents()) {
            if (comp.isVisible()) {
                if (comp instanceof WelcomeView welcomeView) {
                    welcomeView.updateCardDisplay(currentInputBuffer);
                } else if (comp instanceof LoginView loginView) {
                    loginView.updatePinDisplay(currentInputBuffer);
                } else if (comp instanceof ViewMenu viewMenu) {
                    viewMenu.updateMenuInput(currentInputBuffer);
                } else if (comp instanceof ViewTarikTunai viewTarikTunai) {
                    viewTarikTunai.updateInput(currentInputBuffer);
                } else if (comp instanceof ViewTransfer viewTransfer) {
                    viewTransfer.updateInput(currentInputBuffer);
                } else if (comp instanceof ViewSetorTunai viewSetorTunai) {
                    viewSetorTunai.updateInput(currentInputBuffer);
                }
            }
        }
    }

    /**
     * Logika Pusat saat tombol CANCEL ditekan.
     * Berfungsi sebagai "Back Button", "Clear Input", atau "Abort Transaction".
     */
    private void handleCancelPressed() {
        // 1. Selalu bersihkan inputan teks dulu
        inputBuffer.setLength(0);
        currentInputBuffer = "";
        updateActiveView();

        LOGGER.log(
            Level.INFO,
            "[DEBUG] Tombol CANCEL ditekan pada State: {0}",
            mesin.getStateSaatIni().getClass().getSimpleName()
        );


        // 2. Cek State saat ini untuk menentukan aksi "Kembali"
        state.StateATM currentState = mesin.getStateSaatIni();

        if (isReturnToMenuState(currentState)) {
            mesin.ubahState(new state.StateMenuUtama());
            gantiLayar("MENU");
        } else if (currentState instanceof state.StateCekPin) {
            mesin.ubahState(new state.StateSiaga());
            gantiLayar("WELCOME");
        } else if (currentState instanceof state.StateMenuUtama) {
            mesin.keluar();
        }


        // Jika di StateSiaga (Welcome), Cancel hanya menghapus input (sudah dilakukan
        // di langkah 1)
    }
}