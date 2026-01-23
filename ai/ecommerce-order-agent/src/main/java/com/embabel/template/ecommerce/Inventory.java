package com.embabel.template.ecommerce;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Inventory {

    public record Stock(String itemId, int availableQuantity) {}

    @LlmTool(description = "List of current stock levels by item ID")
    public List<Stock> items() {
        return List.of(
                // Electronics
                new Stock("ITM001", 0),
                new Stock("ITM002", 120),
                new Stock("ITM003", 80),
                new Stock("ITM004", 30),
                new Stock("ITM005", 45),
                new Stock("ITM006", 200),
                new Stock("ITM007", 25),
                new Stock("ITM008", 60),
                new Stock("ITM009", 90),
                new Stock("ITM019", 200),

                // Bakery & Dairy
                new Stock("ITM011", 120),
                new Stock("ITM013", 80),
                new Stock("ITM014", 60),
                new Stock("ITM015", 100),
                new Stock("ITM017", 90),
                new Stock("ITM018", 70),

                // Snacks & Confectionery
                new Stock("ITM010", 250),
                new Stock("ITM012", 180),
                new Stock("ITM016", 140),
                new Stock("ITM020", 40)
        );
    }

}
