package com.alex.model;

import lombok.Data;
import org.json.JSONArray;

@Data
public class BittrexSummaries {
    private JSONArray bittrexSummary = new JSONArray();
}