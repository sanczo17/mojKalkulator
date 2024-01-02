import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {
    private final Miner miner;

    public Calculator(Miner miner) {
        this.miner = miner;
    }

    // Oblicza dzienny koszt zużytej energii elektrycznej.
    public double calculateDailyElectricityCost() {
        return (miner.powerConsumption / 1000.0) * 24 * miner.electricityCostPerKWh;
    }

    // Oblicza dzienny zysk netto.
    public double calculateDailyProfit(double cryptoPrice, double dailyCryptoEarning) {
        double dailyRevenue = dailyCryptoEarning * cryptoPrice;
        double dailyElectricityCost = calculateDailyElectricityCost();
        BigDecimal profit = new BigDecimal(dailyRevenue - dailyElectricityCost);
        return profit.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // Oblicza całkowite przychody z wydobycia.
    public double calculateTotalRevenue(double cryptoPrice) {
        return miner.minedCurrencyAmount * cryptoPrice;
    }

    // Oblicza całkowity koszt zużytej energii elektrycznej.
    public double calculateTotalElectricityCost(double daysOperational) {
        return calculateDailyElectricityCost() * daysOperational;
    }
    public double calculateDaysToBreakEven(double cryptoPrice, double dailyCryptoEarning) {
        double totalMinedCurrency = miner.minedCurrencyAmount + dailyCryptoEarning;

        double totalRevenue = totalMinedCurrency * cryptoPrice;
        double remainingInvestment = miner.initialCost - totalRevenue;

        if (remainingInvestment <= 0) {
            return 0; // Inwestycja już się zwróciła
        }
        // Dzienny zysk netto z aktualnej ilości wykopanej waluty
        double dailyNetProfit = dailyCryptoEarning * cryptoPrice - calculateDailyElectricityCost();

        if (dailyNetProfit <= 0) {
            return Double.POSITIVE_INFINITY; // Brak zysku dziennego
        }

        // Dni do zwrotu kosztów
        double daysToBreakEven = remainingInvestment / dailyNetProfit;

        return Math.ceil(daysToBreakEven);
    }
}
