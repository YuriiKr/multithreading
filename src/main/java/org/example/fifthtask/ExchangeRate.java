package org.example.fifthtask;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private BigDecimal rateUsdToHrn = new BigDecimal("33.4");
    private BigDecimal rateUsdToEuro = new BigDecimal("0.9");
    private BigDecimal rateHrnToEuro = new BigDecimal("36.5");
}
