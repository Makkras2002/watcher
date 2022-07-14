package com.currency.watcher.controller;

import com.currency.watcher.entity.CryptoCurrency;
import com.currency.watcher.entity.RegisteredUser;
import com.currency.watcher.repo.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class CryptoCurrencyController {
    private CurrencyRepository currencyRepository;
    public static Set<RegisteredUser> registeredUsers = new HashSet<>();
    @Autowired
    public CryptoCurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/showAvailableCryptoCurrencies")
    public List<CryptoCurrency> showAvailableCryptoCurrencies(){
        return currencyRepository.findAll();
    }
    @GetMapping("/showActualPriceForCryptoCurrency")
    public CryptoCurrency showActualPriceForCryptoCurrency(@RequestParam(value = "code",required = true) String code) {
        Optional<CryptoCurrency> cryptoCurrency = currencyRepository.findBySymbol(code);
        return cryptoCurrency.orElseGet(CryptoCurrency::new);
    }
    @GetMapping("/notify")
    public void notify(@RequestParam(value = "name",required = true) String name,
                       @RequestParam(value = "symbol",required = true) String symbol) {
        Optional<CryptoCurrency> cryptoCurrency = currencyRepository.findBySymbol(symbol);
        cryptoCurrency.ifPresent(currency -> {
            if(registeredUsers.stream().filter(user -> user.getCryptoCurrency()
                    .getSymbol().equals(symbol))
                    .filter(user -> user.getName().equals(name))
                    .toArray().length == 0) {
                registeredUsers.add(new RegisteredUser(name, currency));
            }
        });
    }
}
