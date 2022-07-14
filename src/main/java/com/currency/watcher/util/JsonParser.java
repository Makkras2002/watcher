package com.currency.watcher.util;

import java.util.Set;

public interface JsonParser<T> {
    public Set<T> parseData(String dataInJson);
}
