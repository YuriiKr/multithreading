package org.example.fifthtask;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fifthtask.enums.CurrencyEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private BigDecimal balance;
    private CurrencyEnum currency;
}
