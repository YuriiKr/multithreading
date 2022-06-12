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
        exchangeRates.put(HRN, new BigDecimal("33.4"));
        exchangeRates.put(EURO, new BigDecimal("0.9"));
        User firstUser = repository.readUserFromFile(userOne);
        User secondUser = repository.readUserFromFile(userTwo);
        if (userOneSell) {
            firstUser.setBalance(firstUser.getBalance().subtract(exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency()))));
            secondUser.setBalance(secondUser.getBalance().add(exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency()))));
            logger.info("User " + firstUser.getName() + " sold " + exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency())) + " " + firstUser.getCurrency());
            logger.info("User " + secondUser.getName() + " bought " + exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency())) + " " + secondUser.getCurrency());
        } else
        {
            firstUser.setBalance(firstUser.getBalance().add(exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency()))));
            secondUser.setBalance(secondUser.getBalance().subtract(exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency()))));
            logger.info("User " + firstUser.getName() + " bought " + exchangeAmount.multiply(exchangeRates.get(firstUser.getCurrency())) + " " + firstUser.getCurrency());
            logger.info("User " + secondUser.getName() + " sold " + exchangeAmount.multiply(exchangeRates.get(secondUser.getCurrency())) + " " + secondUser.getCurrency());
        }
        repository.writeUserToFile(firstUser);
        repository.writeUserToFile(secondUser);

    }

}
