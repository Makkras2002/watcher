package com.currency.watcher.service;


import com.currency.watcher.controller.CryptoCurrencyController;
import com.currency.watcher.entity.CryptoCurrency;
import com.currency.watcher.exception.ParsingException;
import com.currency.watcher.exception.RequestSendingException;
import com.currency.watcher.repo.CurrencyRepository;
import com.currency.watcher.service.connection.CurrencyDataRetrieverFromApiService;
import com.currency.watcher.util.impl.CurrencyJsonParserTrackedCurrencies;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CurrencyDataExtractorToDb {
    private static final String RESOURCE_LOCATION = "classpath:tracked_currencies.json";
    private static Logger logger = LogManager.getLogger();
    private CurrencyDataRetrieverFromApiService currencyDataRetrieverFromApiService;
    private CurrencyJsonParserTrackedCurrencies currencyJsonParser;
    private CurrencyRepository currencyRepository;
    private CurrencyPriceChangesAssessorService currencyPriceChangesAssessorService;

    @Autowired
    public CurrencyDataExtractorToDb(CurrencyDataRetrieverFromApiService currencyDataRetrieverService,
                                     CurrencyJsonParserTrackedCurrencies currencyJsonParser,
                                     CurrencyRepository currencyRepository,
                                     CurrencyPriceChangesAssessorService currencyPriceChangesAssessorService) {
        this.currencyDataRetrieverFromApiService = currencyDataRetrieverService;
        this.currencyJsonParser = currencyJsonParser;
        this.currencyRepository = currencyRepository;
        this.currencyPriceChangesAssessorService = currencyPriceChangesAssessorService;
    }

    public void extractCurrencyDataToDb() throws RequestSendingException, ParsingException, IOException {
        List<CryptoCurrency> cryptoCurrencySet = new ArrayList<>();
        try (FileReader fileReader = new FileReader(ResourceUtils.getFile(RESOURCE_LOCATION).getAbsolutePath());
             BufferedReader bufferedReader = new BufferedReader(fileReader)){
            Set<CryptoCurrency> trackedCryptos = currencyJsonParser.parseData(bufferedReader.lines().collect(Collectors.joining()));
            for(CryptoCurrency cryptoCurrency : trackedCryptos) {
                if(currencyDataRetrieverFromApiService.getData(cryptoCurrency.getId()).isPresent()) {
                    cryptoCurrencySet.addAll(currencyDataRetrieverFromApiService.getData(cryptoCurrency.getId()).get());
                }
            }
            List<CryptoCurrency> existingDataInDb = currencyRepository.findAll();
            if(CryptoCurrencyController.registeredUsers.size() != 0) {
                currencyPriceChangesAssessorService.assessPriceChanges(existingDataInDb,cryptoCurrencySet);
            }
            currencyRepository.saveAll(cryptoCurrencySet);
            logger.info("Crypto currencies data was successfully updated.");
        }

    }

}
