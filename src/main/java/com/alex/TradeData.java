package com.alex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeData extends JSONObject {
    private String instrument;
    private String direction;
    private double price;
    private BigDecimal size;
    private BigDecimal total;
    private LocalDateTime time;
}
