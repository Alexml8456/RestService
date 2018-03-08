package com.alex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FreeOrder {
    private BigDecimal price;
    private BigDecimal value;
    private BigDecimal total;
}
