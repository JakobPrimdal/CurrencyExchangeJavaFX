package dk.easv.currencyexchange.be;

// Java imports
import java.time.LocalDateTime;

public class Currency {
    private String baseCurrency;
    private String targetCurrency;
    private double rate = -1;
    private LocalDateTime timestamp;

    public Currency(String baseCurrency, String targetCurrency, double rate, LocalDateTime timestamp) {
        setBaseCurrency(baseCurrency);
        setTargetCurrency(targetCurrency);
        setRate(rate);
        setTimestamp(timestamp);
    }

    private void setBaseCurrency(String baseCurrency) {
        if (baseCurrency != null)
            this.baseCurrency = baseCurrency;
    }

    private void setTargetCurrency(String targetCurrency) {
        if (targetCurrency != null)
            this.targetCurrency = targetCurrency;
    }

    private void setRate(double rate) {
        if (rate != -1)
            this.rate = rate;
    }

    private void setTimestamp(LocalDateTime timestamp) {
        if (timestamp != null)
            this.timestamp = timestamp;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return baseCurrency + "/" + targetCurrency + ": " + rate;
    }

}
