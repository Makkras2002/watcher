package com.currency.watcher.service;

import com.currency.watcher.controller.CryptoCurrencyController;
import com.currency.watcher.entity.CryptoCurrency;
import com.currency.watcher.entity.RegisteredUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CurrencyPriceChangesAssessorService {
    private static Logger logger = LogManager.getLogger();
    public void assessPriceChanges(List<CryptoCurrency> oldData, List<CryptoCurrency> newData) {
        for(CryptoCurrency oldCryptoCurrency : oldData) {
            CryptoCurrency newCurrency = newData.stream().filter(currency -> currency.getId()
                    .equals(oldCryptoCurrency.getId())).findFirst().get();
            Double oldPrice = oldCryptoCurrency.getPrice().doubleValue();
            Double newPrice = newCurrency.getPrice().doubleValue();
            if(oldCryptoCurrency.getPrice().compareTo(newCurrency.getPrice()) == 1 ||
                    oldCryptoCurrency.getPrice().compareTo(newCurrency.getPrice()) == -1) {
                if(Math.abs((oldPrice - newPrice)/
                        oldPrice) * 100 > 1) {
                    CryptoCurrencyController.registeredUsers.stream()
                            .filter(user -> user.getCryptoCurrency().getSymbol().equals(oldCryptoCurrency.getSymbol())).forEach(user -> {
                        Double registeredPrice = user.getCryptoCurrency().getPrice().doubleValue();
                        Double finalPercentChange;
                        String changeDirection;
                        if(registeredPrice > newPrice) {
                            finalPercentChange = ((registeredPrice - newPrice)/
                                    registeredPrice) * 100;
                            changeDirection = " decreased on ";
                        } else {
                            finalPercentChange = ((newPrice - registeredPrice)/
                                    registeredPrice) * 100;
                            changeDirection = " increased on ";
                        }
                        logger.warn(user.getCryptoCurrency().getSymbol()
                                + " " + user.getName()
                                + changeDirection + finalPercentChange + "%");
                            });
                }
            }

        }
    }
}
