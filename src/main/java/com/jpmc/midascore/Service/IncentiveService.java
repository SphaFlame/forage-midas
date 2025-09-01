package com.jpmc.midascore.Service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    private static final Logger logger = LoggerFactory.getLogger(IncentiveService.class);
    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    @Autowired
    private RestTemplate restTemplate;

    public float getIncentive(Transaction transaction) {
        try {
            logger.info("Calling incentive API for transaction: {}", transaction);

            ResponseEntity<Incentive> response = restTemplate.postForEntity(
                    INCENTIVE_API_URL,
                    transaction,
                    Incentive.class
            );

            if (response.getBody() != null) {
                float incentiveAmount = response.getBody().getAmount();
                logger.info("Received incentive amount: {}", incentiveAmount);
                return incentiveAmount;
            } else {
                logger.warn("Received null response from incentive API");
                return 0.0f;
            }

        } catch (Exception e) {
            logger.error("Error calling incentive API: {}", e.getMessage(), e);
            return 0.0f; // Return 0 if API call fails
        }
    }
}
