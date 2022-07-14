package com.currency.watcher.scheduler;

import com.currency.watcher.exception.ParsingException;
import com.currency.watcher.exception.RequestSendingException;
import com.currency.watcher.service.CurrencyDataExtractorToDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomCurrencyRetrievalScheduler {

    private static final String PERIOD = "0 * * * * *";//Every minute
    private static Logger logger = LogManager.getLogger();
    private CurrencyDataExtractorToDb dataExtractorToDb;

    @Autowired
    public CustomCurrencyRetrievalScheduler(CurrencyDataExtractorToDb dataExtractorToDb) {
        this.dataExtractorToDb = dataExtractorToDb;
    }

    @Scheduled(cron = PERIOD)
    public void createTasks() {
        try {
            logger.info("Crypto currencies data updating...");
            dataExtractorToDb.extractCurrencyDataToDb();
        } catch (RequestSendingException | ParsingException | IOException e) {
            logger.error(e.getMessage());
        }
    }
}