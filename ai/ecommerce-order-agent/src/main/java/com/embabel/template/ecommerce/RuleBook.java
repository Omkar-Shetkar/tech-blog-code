package com.embabel.template.ecommerce;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

@Component
public class RuleBook {

    @LlmTool(description = "Rules to decide whether an item in the order is eligible for replacement or not")
    public String rules() {
        return """
                - Check whether the item is eatable or not.
                - Eatable items like bakery, dairy, snacks products aren't eligible for replacement.
                - Non-eatable items like Electronics, Books are eligible for replacement.
                """;
    }
}
