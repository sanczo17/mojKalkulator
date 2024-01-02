import java.io.*;
import java.util.Locale;
public class Miner {
    double hashrate;
    double powerConsumption;
    double initialCost;
    double electricityCostPerKWh;
    double dailyProfit;
    double minedCurrencyAmount;
    double totalElectricityCost;
    double lastAddedCurrency;
    double dailyEnergyUsage;
    double totalEnergyConsumed;
    private double pendingMinedCurrency = 0;

    public Miner(double hashrate, double powerConsumption, double initialCost, double electricityCostPerKWh) {
        this.hashrate = hashrate;
        this.powerConsumption = powerConsumption;
        this.initialCost = initialCost;
        this.electricityCostPerKWh = electricityCostPerKWh;
        this.totalElectricityCost = 0;
        this.lastAddedCurrency = 0;
        this.dailyEnergyUsage = calculateDailyEnergyUsage();
        this.totalEnergyConsumed = 0;
    }

    public void addPendingMinedCurrency(double additionalAmount) {
        this.pendingMinedCurrency += additionalAmount;
    }

    public void updateDailyProfit(double newProfit) {
        this.dailyProfit = newProfit;
    }

    public void updateTotalElectricityCost(double electricityCostPerKWh) {
        this.totalElectricityCost += calculateDailyEnergyUsage() * electricityCostPerKWh;
    }

    public double calculateDailyEnergyUsage() {
        return (this.powerConsumption / 1000.0) * 24;
    }

    public void saveToFile(String filename) {
        this.minedCurrencyAmount += this.pendingMinedCurrency;
        this.pendingMinedCurrency = 0;
        this.totalEnergyConsumed += this.dailyEnergyUsage;

        double currentCost = this.totalEnergyConsumed * this.electricityCostPerKWh;
        String formattedTotalEnergyConsumed = String.format(Locale.ROOT, "%.2f", this.totalEnergyConsumed);
        String formattedCost = String.format(Locale.ROOT, "%.2f", currentCost);

        try (PrintWriter out = new PrintWriter(filename)) {
            out.println("Moc koparki = " + hashrate + " Th/s");
            out.println("Pobor = " + powerConsumption + " Watt");
            out.println("Cena zakupu koparki = " + initialCost + " PLN");
            out.println("Cena za 1kWh = " + electricityCostPerKWh + " PLN");
            out.println("Dzienny zysk = " + dailyProfit + " PLN");
            out.println("Calosc zuzytej energii = " + formattedTotalEnergyConsumed + " kWh");
            out.println("Calosc kosztu zuzytej energii = " + formattedCost + " PLN");
            out.println("Ilosc wykopanej waluty = " + minedCurrencyAmount);
        } catch (FileNotFoundException e) {
            System.out.println("Nie udało się zapisać do pliku: " + filename + ". " + e.getMessage());
        }
    }
    public static Miner loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            double hashrate = 0, powerConsumption = 0, initialCost = 0, electricityCostPerKWh = 0, dailyProfit = 0, totalEnergyConsumed = 0, totalElectricityCost = 0, minedCurrencyAmount = 0;

            if ((line = br.readLine()) != null) {
                hashrate = Double.parseDouble(line.split("=")[1].trim().replace(" Th/s", "").replace(",", "."));
            }
            if ((line = br.readLine()) != null) {
                powerConsumption = Double.parseDouble(line.split("=")[1].trim().replace(" Watt", "").replace(",", "."));
            }
            if ((line = br.readLine()) != null) {
                initialCost = Double.parseDouble(line.split("=")[1].trim().replace(" PLN", "").replace(",", "."));
            }
            if ((line = br.readLine()) != null) {
                electricityCostPerKWh = Double.parseDouble(line.split("=")[1].trim().replace(" PLN za kWh", "").replace(" PLN", "").replace(",", "."));
            }
            if ((line = br.readLine()) != null) {
                dailyProfit = Double.parseDouble(line.split("=")[1].trim().replace(" PLN", "").replace(",", "."));
            }
            if ((line = br.readLine()) != null) {
                totalEnergyConsumed = Double.parseDouble(line.split("=")[1].trim().replace(" kWh", "").replace(",", "."));
            }
            if ((line = br.readLine()) != null) {
                totalElectricityCost = Double.parseDouble(line.split("=")[1].trim().replace(" PLN", "").replace(",", "."));
            }
            if ((line = br.readLine()) != null) {
                minedCurrencyAmount = Double.parseDouble(line.split("=")[1].trim().replace(",", "."));
            }

            Miner miner = new Miner(hashrate, powerConsumption, initialCost, electricityCostPerKWh);
            miner.dailyProfit = dailyProfit;
            miner.minedCurrencyAmount = minedCurrencyAmount;
            miner.totalElectricityCost = totalElectricityCost;
            miner.totalEnergyConsumed = totalEnergyConsumed;
            miner.pendingMinedCurrency = 0;
            return miner;

        } catch (IOException | NumberFormatException e) {
            System.out.println("Wystąpił problem z odczytem pliku: " + filename + ". " + e.getMessage());
            return null;
        }
    }
}