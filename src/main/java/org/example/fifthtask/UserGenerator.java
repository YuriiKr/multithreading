package org.example.fifthtask;

import org.example.fifthtask.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserGenerator {

    private static UserRepository repository = new UserRepository();

    private static final Random RANDOM = new Random();
    private static final CurrencyEnum[] currencyEnums = CurrencyEnum.values();
    private static final int currencyEnumsSize = currencyEnums.length;

    public static List<User> generateUsers (Integer usersAmount) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < usersAmount; i++) {
            User user = new User("User"+i, BigDecimal.valueOf(Math.random() * 10000),
                    currencyEnums[RANDOM.nextInt(currencyEnumsSize)]);
            repository.writeUserToFile(user);
            users.add(user);
        }
        return users;
    }
}
