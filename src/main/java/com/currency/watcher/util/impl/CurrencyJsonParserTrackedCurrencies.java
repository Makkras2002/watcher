package com.currency.watcher.util.impl;

import com.currency.watcher.entity.CryptoCurrency;
import com.currency.watcher.util.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class CurrencyJsonParserTrackedCurrencies implements JsonParser<CryptoCurrency> {
    public Set<CryptoCurrency> parseData(String dataInJson) {
        Set<CryptoCurrency> cryptoCurrencySet = new HashSet<>();
        JSONObject jsonObject = new JSONObject(dataInJson);
        JSONArray jsonArray = jsonObject.getJSONArray("currencies");
        for(int i = 0; i < jsonArray.length();i++) {
            cryptoCurrencySet.add(new CryptoCurrency(jsonArray.getJSONObject(i).getLong("id"),
                    jsonArray.getJSONObject(i).getString("symbol"),null,null));
        }
        return cryptoCurrencySet;
    }
}
