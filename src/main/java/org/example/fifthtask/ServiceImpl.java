package org.example.fifthtask;

import java.math.BigDecimal;

public class ServiceImpl {
    ExchangeRate exchangeRate = new ExchangeRate();



    public BigDecimal exchangeUsdToHrn (BigDecimal usd) {
        BigDecimal rate = exchangeRate.getRateUsdToHrn();
        return usd.multiply(rate);
    }

    public BigDecimal exchangeHrnToUsd (BigDecimal hrn) {
        BigDecimal rate = exchangeRate.getRateUsdToHrn();
        return hrn.divide(rate);
    }

    public BigDecimal exchangeUsdToEuro (BigDecimal usd) {
        BigDecimal rate = exchangeRate.getRateUsdToEuro();
        return usd.multiply(rate);
    }

    public BigDecimal exchangeEuroToUsd (BigDecimal eur) {
        BigDecimal rate = exchangeRate.getRateUsdToHrn();
        return eur.divide(rate);
    }

    public BigDecimal exchangeEuroToHrn (BigDecimal eur) {
        BigDecimal rate = exchangeRate.getRateHrnToEuro();
        return eur.multiply(rate);
    }

    public BigDecimal exchangeHrnToEur (BigDecimal hrn) {
        BigDecimal rate = exchangeRate.getRateHrnToEuro();
        return hrn.divide(rate);
    }

}
