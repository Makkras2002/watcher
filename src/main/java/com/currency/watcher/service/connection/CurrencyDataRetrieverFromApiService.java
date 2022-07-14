package com.currency.watcher.service.connection;

import com.currency.watcher.entity.CryptoCurrency;
import com.currency.watcher.exception.ParsingException;
import com.currency.watcher.exception.RequestSendingException;
import com.currency.watcher.util.JsonParser;
import com.currency.watcher.util.impl.CurrencyJsonParserFromApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class CurrencyDataRetrieverFromApiService {

    private static Logger logger = LogManager.getLogger();

    private final RestTemplate restTemplate;
    private JsonParser<CryptoCurrency> currenciesFromApiJsonParser;

    @Autowired
    public CurrencyDataRetrieverFromApiService(RestTemplateBuilder restTemplateBuilder,
                                               CurrencyJsonParserFromApi currenciesFromApiJsonParser) {
        this.restTemplate = restTemplateBuilder.build();
        this.currenciesFromApiJsonParser = currenciesFromApiJsonParser;
    }

    public Optional<Set<CryptoCurrency>> getData(Long currency_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setCacheControl(CacheControl.maxAge(Duration.ZERO));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XHTML_XML));
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(ConnectionURL.ACQUIRE_CURRENCY_DATA,
                HttpMethod.GET,entity,String.class, currency_id);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Optional.ofNullable(currenciesFromApiJsonParser.parseData(response.getBody()));
        } else {
            logger.warn("Request Failed");
            return Optional.empty();
        }

    }
}
