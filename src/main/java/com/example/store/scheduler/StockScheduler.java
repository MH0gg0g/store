package com.example.store.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.store.entities.Product;
import com.example.store.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockScheduler {
    private static final long STOCK_THRSHOLD = 20;

    private final ProductRepository productRepository;

    @Async
    @Scheduled(cron = "0 * * * * *")
    public void checkLowStockLevels() {
        List<Product> products = productRepository.findAll();

        for (var p : products) {
            if (p.getQuantity() < STOCK_THRSHOLD) {
                log.warn(
                        "low stock alert: Product ID: {}, Name: {} has only {} units",
                        p.getId(),
                        p.getName(),
                        p.getQuantity());
            }
        }
    }
}
