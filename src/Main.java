public class Main {
    public static void main(String[] args) {
        UserInteraction ui = new UserInteraction();
        Miner miner;
        Calculator calculator;
        String fileName;
        double electricityCostPerKWh;

        String answer = ui.askYesNoQuestion("Czy chcesz wczytac dane z pliku?");

        if ("tak".equals(answer)) {
            fileName = ui.askForFileName("Podaj nazwe pliku do wczytania:");
            miner = FileHandler.loadFromFile(fileName);
            if (miner == null) {
                System.out.println("Nie udalo się wczytac danych. Sprawdz plik i sprobuj ponownie.");
                return;
            }
            // Przypuszczam, że koszt energii elektrycznej jest również wczytywany z pliku.
            electricityCostPerKWh = miner.electricityCostPerKWh;
        } else {
            fileName = ui.askForFileName("Podaj nazwe pliku do zapisu:");
            double hashrate = ui.askForDouble("Podaj hashrate koparki (TH/s): ");
            double powerConsumption = ui.askForDouble("Podaj pobor mocy koparki (W): ");
            double initialCost = ui.askForDouble("Podaj koszt koparki (PLN): ");
            electricityCostPerKWh = ui.askForDouble("Podaj koszt energii (PLN za kWh): ");
            miner = new Miner(hashrate, powerConsumption, initialCost, electricityCostPerKWh);
        }

        calculator = new Calculator(miner);

        double cryptoPrice = ui.askForDouble("Podaj aktualna cene kryptowaluty (PLN): ");
        double dailyCryptoEarning = ui.askForDouble("Podaj dzienny przychod z wydobycia (w kryptowalucie): ");

        miner.addPendingMinedCurrency(dailyCryptoEarning);
        miner.updateTotalElectricityCost(electricityCostPerKWh);

        double dailyProfit = calculator.calculateDailyProfit(cryptoPrice, dailyCryptoEarning);
        miner.updateDailyProfit(dailyProfit);

        double daysToBreakEven = calculator.calculateDaysToBreakEven(cryptoPrice, dailyCryptoEarning);
        System.out.println("Dzienny zysk: " + dailyProfit + " PLN");
        System.out.println("Szacowany czas do zwrotu inwestycji: " + daysToBreakEven + " dni");

        FileHandler.saveToFile(miner, fileName);
    }
}
