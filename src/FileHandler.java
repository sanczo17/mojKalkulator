import java.io.*;

public class FileHandler {
    public static Miner loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            double hashrate = Double.parseDouble(br.readLine().split("=")[1].trim().split(" ")[0]);
            double powerConsumption = Double.parseDouble(br.readLine().split("=")[1].trim().split(" ")[0]);
            double initialCost = Double.parseDouble(br.readLine().split("=")[1].trim().split(" ")[0]);
            double electricityCostPerKWh = Double.parseDouble(br.readLine().split("=")[1].trim().split(" ")[0]);
            double dailyProfit = Double.parseDouble(br.readLine().split("=")[1].trim().split(" ")[0]);
            double minedCurrencyAmount = Double.parseDouble(br.readLine().split("=")[1].trim());

            Miner miner = new Miner(hashrate, powerConsumption, initialCost, electricityCostPerKWh);
            miner.updateDailyProfit(dailyProfit);
            miner.addPendingMinedCurrency(minedCurrencyAmount);
            return miner;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Nie udalo sie wczytac danych z pliku: " + filename);
            return null;
        }
    }

    public static void saveToFile(Miner miner, String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println("Moc koparki = " + miner.hashrate + " Th/s");
            out.println("Pobor = " + miner.powerConsumption + " Watt");
            out.println("Cena zakupu koparki = " + miner.initialCost + " PLN");
            out.println("Cena za 1kWh = " + miner.electricityCostPerKWh + " PLN");
            out.println("Dzienny zysk = " + miner.dailyProfit + " PLN");
            out.println("Ilosc wykopanej waluty = " + miner.minedCurrencyAmount);
        } catch (FileNotFoundException e) {
            System.out.println("Nie udalo sie zapisac do pliku: " + filename);
        }
    }
}
