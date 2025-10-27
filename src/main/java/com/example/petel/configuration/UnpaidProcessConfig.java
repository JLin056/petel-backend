package com.example.petel.configuration;

import com.example.petel.service.BOOK012Svc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class UnpaidProcessConfig {

    @Autowired
    private BOOK012Svc book012Svc;

    @Scheduled(fixedRate = 60000) // 一分鐘執行一次
    public void cancelPendingOrders() throws Exception {
        book012Svc.book012();
    }
}
