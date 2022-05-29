package org.example.fifthtask;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private BigDecimal hrnAmount = BigDecimal.valueOf(Math.random()*10000);
    private BigDecimal usdAmount = BigDecimal.valueOf(Math.random()*10000);
    private BigDecimal euroAmount = BigDecimal.valueOf(Math.random()*10000);

    public User (String name) {
        this.name = name;
    }
}
