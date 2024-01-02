import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MiningCalculatorGUI extends JFrame {

    private ResourceBundle messages;
    private Miner currentMiner;
    private String currentFilePath;
    private final JTextField hashrateField = new JTextField();
    private final JTextField powerConsumptionField = new JTextField();
    private final JTextField initialCostField = new JTextField();
    private final JTextField electricityCostField = new JTextField();
    private final JTextField cryptoPriceField = new JTextField();
    private final JTextField dailyEarningField = new JTextField();
    private final JLabel hashrateLabel = new JLabel();
    private final JLabel powerConsumptionLabel = new JLabel();
    private final JLabel initialCostLabel = new JLabel();
    private final JLabel electricityCostLabel = new JLabel();
    private final JLabel cryptoPriceLabel = new JLabel();
    private final JLabel dailyEarningLabel = new JLabel();
    private final JLabel resultLabel = new JLabel();
    private final JLabel minedCurrencyLabel = new JLabel();
    private final JButton calculateButton = new JButton();

    public MiningCalculatorGUI() {
        setLanguage("pl-PL");
        setTitle("Mining Calculator");
        setSize(500, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        addMenuBar();
        addComponents();
        add(bottomPanel(), BorderLayout.SOUTH);

        calculateButton.addActionListener(this::calculateAction);

        setVisible(true);
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu(messages.getString("menu"));
        menuBar.add(menu);

        JMenuItem menuItemLoad = new JMenuItem(messages.getString("load"));
        menuItemLoad.addActionListener(this::loadAction);
        menu.add(menuItemLoad);

        JMenuItem menuItemSave = new JMenuItem(messages.getString("save"));
        menuItemSave.addActionListener(this::saveAction);
        menu.add(menuItemSave);

        JMenuItem menuItemSwitchLanguage = new JMenuItem(messages.getString("switch_language"));
        menuItemSwitchLanguage.addActionListener(this::switchLanguageAction);
        menu.add(menuItemSwitchLanguage);

        setJMenuBar(menuBar);
    }

    private void addComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        Dimension fieldDimension = new Dimension(100, 25);

        hashrateField.setPreferredSize(fieldDimension);
        powerConsumptionField.setPreferredSize(fieldDimension);
        initialCostField.setPreferredSize(fieldDimension);
        electricityCostField.setPreferredSize(fieldDimension);
        cryptoPriceField.setPreferredSize(fieldDimension);
        dailyEarningField.setPreferredSize(fieldDimension);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(hashrateLabel, gbc);
        gbc.gridx = 1;
        panel.add(hashrateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(powerConsumptionLabel, gbc);
        gbc.gridx = 1;
        panel.add(powerConsumptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(initialCostLabel, gbc);
        gbc.gridx = 1;
        panel.add(initialCostField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(electricityCostLabel, gbc);
        gbc.gridx = 1;
        panel.add(electricityCostField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(cryptoPriceLabel, gbc);
        gbc.gridx = 1;
        panel.add(cryptoPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(dailyEarningLabel, gbc);
        gbc.gridx = 1;
        panel.add(dailyEarningField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(resultLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(minedCurrencyLabel, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private JPanel bottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));

        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(calculateButton);

        Dimension minSize = new Dimension(0, 5);
        Dimension prefSize = new Dimension(0, 15);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 15);
        bottomPanel.add(new Box.Filler(minSize, prefSize, maxSize));

        return bottomPanel;
    }

    private void setLanguage(String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        updateTexts();
    }

    private void switchLanguageAction(ActionEvent e) {
        Locale currentLocale = messages.getLocale();
        if ("en".equals(currentLocale.getLanguage()) && "US".equals(currentLocale.getCountry())) {
            setLanguage("pl-PL");
        } else {
            setLanguage("en-US");
        }
    }

    private void updateTexts() {
        setTitle(messages.getString("title"));

        hashrateLabel.setText(messages.getString("hashrate"));
        powerConsumptionLabel.setText(messages.getString("power_consumption"));
        initialCostLabel.setText(messages.getString("initial_cost"));
        electricityCostLabel.setText(messages.getString("electricity_cost"));
        cryptoPriceLabel.setText(messages.getString("crypto_price"));
        dailyEarningLabel.setText(messages.getString("daily_earning"));
        calculateButton.setText(messages.getString("calculate"));

        if (currentMiner != null) {
            minedCurrencyLabel.setText(String.format("<html>%s: %s" +
                            "<br>%s: %.2f kWh" +
                            "<br>%s: %.2f PLN</html>",
                    messages.getString("minedCurrency"), currentMiner.minedCurrencyAmount,
                    messages.getString("totalEnergyConsumed"), currentMiner.totalEnergyConsumed,
                    messages.getString("totalElectricityCost"), currentMiner.totalElectricityCost));
        }
    }

    private void calculateAction(ActionEvent e) {
        performCalculation();
    }

    private void loadAction(ActionEvent e) {
        performFileOperation(true);
    }

    private void saveAction(ActionEvent e) {
        performFileOperation(false);
    }
    private double parseDouble(String input) {
        if (input != null) {
            return Double.parseDouble(input.replace(',', '.'));
        }
        return 0.0;
    }
    private void performCalculation() {
        try {
            if (currentFilePath != null && !currentFilePath.isEmpty()) {
                currentMiner = Miner.loadFromFile(currentFilePath);
            }
            if (currentMiner == null) {
                double hashrate = parseDouble(hashrateField.getText());
                double powerConsumption = parseDouble(powerConsumptionField.getText());
                double initialCost = parseDouble(initialCostField.getText());
                double electricityCostPerKWh = parseDouble(electricityCostField.getText());
                currentMiner = new Miner(hashrate, powerConsumption, initialCost, electricityCostPerKWh);
            }
            double cryptoPrice = parseDouble(cryptoPriceField.getText());
            double dailyCryptoEarning = parseDouble(dailyEarningField.getText());
            currentMiner.addPendingMinedCurrency(dailyCryptoEarning);

            Calculator calculator = new Calculator(currentMiner);
            double dailyProfit = calculator.calculateDailyProfit(cryptoPrice, dailyCryptoEarning);
            double daysToBreakEven = calculator.calculateDaysToBreakEven(cryptoPrice, dailyCryptoEarning);

            resultLabel.setText(String.format("<html>%s: %.2f PLN<br>%s: %.0f</html>",
                    messages.getString("dailyProfitMessage"),
                    dailyProfit,
                    messages.getString("daysToBreakEven"),
                    daysToBreakEven));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, messages.getString("calculateError"), messages.getString("invalidData"), JOptionPane.ERROR_MESSAGE);
        }
    }
    private void performFileOperation(boolean load) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plik tekstowy", "txt"));

        int result = load ? fileChooser.showOpenDialog(this) : fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fileToOperate = fileChooser.getSelectedFile();
            currentFilePath = fileToOperate.getAbsolutePath(); // Zapisz ścieżkę
            if (!currentFilePath.toLowerCase().endsWith(".txt")) {
                currentFilePath += ".txt";
            }

            if (load) {
                currentMiner = Miner.loadFromFile(currentFilePath);
                if (currentMiner != null) {
                    hashrateField.setText(String.valueOf(currentMiner.hashrate));
                    powerConsumptionField.setText(String.valueOf(currentMiner.powerConsumption));
                    initialCostField.setText(String.valueOf(currentMiner.initialCost));
                    electricityCostField.setText(String.valueOf(currentMiner.electricityCostPerKWh));
                    dailyEarningField.setText("0");

                    double dailyEnergyUsage = currentMiner.calculateDailyEnergyUsage();
                    resultLabel.setText(messages.getString("dataLoadedSuccessfully") + " " + fileToOperate.getName());
                    minedCurrencyLabel.setText(String.format("<html>%s: %s" +
                                    "<br>%s: %.2f kWh" +
                                    "<br>%s: %.2f PLN</html>",
                            messages.getString("minedCurrency"), currentMiner.minedCurrencyAmount,
                            messages.getString("totalEnergyConsumed"), currentMiner.totalEnergyConsumed,
                            messages.getString("totalElectricityCost"), currentMiner.totalElectricityCost));
                } else {
                    resultLabel.setText(messages.getString("failedToLoadData"));
                }
            } else {
                try {
                    double hashrate = Double.parseDouble(hashrateField.getText());
                    double powerConsumption = Double.parseDouble(powerConsumptionField.getText());
                    double initialCost = Double.parseDouble(initialCostField.getText());
                    double electricityCostPerKWh = Double.parseDouble(electricityCostField.getText());
                    double additionalMinedCurrency = Double.parseDouble(dailyEarningField.getText());

                    if (currentMiner == null) {
                        currentMiner = new Miner(hashrate, powerConsumption, initialCost, electricityCostPerKWh);
                    }

                    currentMiner.hashrate = hashrate;
                    currentMiner.powerConsumption = powerConsumption;
                    currentMiner.initialCost = initialCost;
                    currentMiner.electricityCostPerKWh = electricityCostPerKWh;
                    currentMiner.addPendingMinedCurrency(additionalMinedCurrency);
                    currentMiner.updateTotalElectricityCost(electricityCostPerKWh);

                    currentMiner.saveToFile(currentFilePath);
                    resultLabel.setText(messages.getString("dataSavedToFile") + fileToOperate.getName());
                    currentMiner = Miner.loadFromFile(currentFilePath);
                    updateGUIWithCurrentMinerData();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, messages.getString("calculateError"), messages.getString("invalidData"), JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, messages.getString("fileSaveError") + ex.getMessage(), messages.getString("invalidData"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
   private void updateGUIWithCurrentMinerData() {
        if (currentMiner != null) {
            hashrateField.setText(String.valueOf(currentMiner.hashrate));
            powerConsumptionField.setText(String.valueOf(currentMiner.powerConsumption));
            initialCostField.setText(String.valueOf(currentMiner.initialCost));
            electricityCostField.setText(String.valueOf(currentMiner.electricityCostPerKWh));
            dailyEarningField.setText("0");

            double dailyEnergyUsage = currentMiner.calculateDailyEnergyUsage();
            resultLabel.setText(messages.getString("dataLoadedSuccessfully"));
            minedCurrencyLabel.setText(String.format("<html>%s: %s" +
                            "<br>%s: %s kWh" +
                            "<br>%s: %s PLN</html>",
                    messages.getString("minedCurrency"), currentMiner.minedCurrencyAmount,
                    messages.getString("totalEnergyConsumed"), String.format("%.2f", currentMiner.totalEnergyConsumed),
                    messages.getString("totalElectricityCost"), String.format("%.2f", currentMiner.totalElectricityCost)));
        }
    }
    public static void main(String[] args) {
        new MiningCalculatorGUI();
    }
}