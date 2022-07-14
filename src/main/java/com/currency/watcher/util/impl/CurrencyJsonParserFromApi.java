package com.currency.watcher.util.impl;

import com.currency.watcher.entity.CryptoCurrency;
import com.currency.watcher.util.JsonParser;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CurrencyJsonParserFromApi implements JsonParser<CryptoCurrency> {
    public Set<CryptoCurrency> parseData(String dataInJson) {
        Set<CryptoCurrency> cryptoCurrencySet = new HashSet<>();
        JSONArray jsonArray = new JSONArray(dataInJson);
        for(int i = 0; i < jsonArray.length();i++) {
            cryptoCurrencySet.add(new CryptoCurrency(jsonArray.getJSONObject(i).getLong("id"),
                    jsonArray.getJSONObject(i).getString("symbol"),jsonArray.getJSONObject(i).getString("name"),
                    jsonArray.getJSONObject(i).getBigDecimal("price_usd")));
        }
        return cryptoCurrencySet;
    }
}
