package org.example.fifthtask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.fifthtask.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.example.fifthtask.enums.CurrencyEnum.*;

public class ExchangeService {
    private static UserRepository repository = new UserRepository();
    private static final Logger logger = LogManager.getLogger(FifthTask.class);

    public void exchange(User userOne, User userTwo, BigDecimal exchangeAmount, boolean userOneSell) {
        Map<CurrencyEnum, BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put(HRN, new BigDecimal("35.3"));
        exchangeRates.put(USD, new BigDecimal("1"));
        exchangeRates.put(EURO, new BigDecimal("0.9"));
        User firstUser = repository.readUserFromFile(userOne);
        User secondUser = repository.readUserFromFile(userTwo);
        if (userOneSell) {
            BigDecimal firstUserResult = firstUser.getBalance().subtract(exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency())));
            if (firstUserResult.compareTo(new BigDecimal("0"))<0) {
                throw new IllegalArgumentException("User " + firstUser.getName() + " doesn't have enough money to exchange");
            }
            firstUser.setBalance(firstUserResult);
            secondUser.setBalance(secondUser.getBalance().add(exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency()))));
            logger.info("User " + firstUser.getName() + " sold " + exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency()))
                    .setScale(2, RoundingMode.HALF_EVEN)+ " " + firstUser.getCurrency());
            logger.info("User " + secondUser.getName() + " bought " + exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency()))
                    .setScale(2, RoundingMode.HALF_EVEN)+ " " + secondUser.getCurrency());
        } else
        {
            BigDecimal secondUserResult = secondUser.getBalance().subtract(exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency())));
            if (secondUserResult.compareTo(new BigDecimal("0"))<0) {
                throw new IllegalArgumentException("User " + secondUser.getName() + " doesn't have enough money to exchange");
            }
            firstUser.setBalance(firstUser.getBalance().add(exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency()))));
            secondUser.setBalance(secondUserResult);
            logger.info("User " + firstUser.getName() + " bought " + exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency()))
                    .setScale(2, RoundingMode.HALF_EVEN) + " " + firstUser.getCurrency());
            logger.info("User " + secondUser.getName() + " sold " + exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency()))
                    .setScale(2, RoundingMode.HALF_EVEN) + " " + secondUser.getCurrency());
        }
        repository.writeUserToFile(firstUser);
        repository.writeUserToFile(secondUser);

    }

}
