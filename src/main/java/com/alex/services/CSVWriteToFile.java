package com.alex.services;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

@Service
@Slf4j
public class CSVWriteToFile {

    @Autowired
    private BittrexSummary bittrexSummary;


    public void wrightToCSV() {
        File file = new File("./result.csv");
        try {
            FileWriter outputFile = new FileWriter(file, true);
            CSVWriter writer = new CSVWriter(outputFile, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            String[] header = {
                    "MarketName", "Time", "High", "Low", "Volume", "BaseVolume", "OpenBuyOrders", "OpenSellOrders",
                    "TotalBuyVolume", "TotalSellVolume", "BuyRatio", "BuyCoefficient", "LastPrice", "PrevDayPrice",
                    "AverageBuyPrice", "AverageSellPrice"
            };
            if (!bittrexSummary.getBittrexSummarys().isEmpty()) {
                if (file.length() == 0) {
                    writer.writeNext(header);
                }
                bittrexSummary.getBittrexSummarys().entrySet().forEach(pair -> {
                    if (pair.getValue().getBuyCoefficient() != null) {
                        String[] data = {
                                pair.getKey(),
                                pair.getValue().getTime(),
                                pair.getValue().getDayHighestPrice().toPlainString(),
                                pair.getValue().getDayLowestPrice().toPlainString(),
                                pair.getValue().getVolume().toPlainString(),
                                pair.getValue().getBaseVolume().toPlainString(),
                                pair.getValue().getOpenBuyOrders().toString(),
                                pair.getValue().getOpenSellOrders().toString(),
                                BigDecimal.valueOf(pair.getValue().getTotalBuyBTC()).toPlainString(),
                                BigDecimal.valueOf(pair.getValue().getTotalSellBTC()).toPlainString(),
                                pair.getValue().getBuyRatio().toString(),
                                pair.getValue().getBuyCoefficient().toString(),
                                pair.getValue().getLastPrice().toPlainString(),
                                pair.getValue().getPrevDayPrice().toPlainString(),
                                BigDecimal.valueOf(pair.getValue().getAverageBuyPrice()).toPlainString(),
                                BigDecimal.valueOf(pair.getValue().getAverageSellPrice()).toPlainString()
                        };
                        writer.writeNext(data);
                    }
                });
                writer.flush();
                writer.close();
                log.info("Data was successfully saved to file!");
            }
        } catch (IOException e) {
            log.error("Can't save data to file. " + e.getMessage(), e);
        }
    }
}
