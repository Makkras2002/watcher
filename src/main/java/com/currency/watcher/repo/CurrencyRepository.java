package com.currency.watcher.repo;


import com.currency.watcher.entity.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository()
public interface CurrencyRepository extends JpaRepository<CryptoCurrency,Long> {
    Optional<CryptoCurrency> findBySymbol(String symbol);
}
