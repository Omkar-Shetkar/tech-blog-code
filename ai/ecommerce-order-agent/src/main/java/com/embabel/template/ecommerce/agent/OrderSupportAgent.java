package com.embabel.template.ecommerce.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.annotation.Condition;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.prompt.persona.Persona;
import com.embabel.common.ai.model.LlmOptions;
import com.embabel.template.ecommerce.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

abstract class Personas {
    static final Persona CUSTOMER_REPRESENTATIVE = Persona.create(
            "Customer Representative",
            "Customer Representative",
            "Professional and helpful",
            "Help customer on his/her order replacement"
    );

    static final Persona STORE_REPRESENTATIVE = Persona.create(
            "Store Representative",
            "Store Representative",
            "Professional and precise",
            "Help Customer Representative with order replacement delivery"
    );
}

@Agent(description = "Support agent to check replacement of items and schedule delivery of replacement item")
public class OrderSupportAgent {

    private final RuleBook ruleBook;
    private final Orders orders;
    private final Inventory inventory;

    static final Logger logger = LoggerFactory.getLogger(OrderSupportAgent.class);
    private final Catalogue catalogue;

    public OrderSupportAgent(RuleBook ruleBook, Orders orders, Inventory inventory, Catalogue catalogue) {
        this.ruleBook = ruleBook;
        this.orders = orders;
        this.inventory = inventory;
        this.catalogue = catalogue;
    }

    public record ReplacementReport(String text, boolean isEligible) {
    }

    public record StockReport(String text, boolean hasStock) {
    }

    public record DeliveryReport(String text) {
    }

    public record CustomerConfirmationReport(String text) {
    }

    @Action(description = "Received order replacement request from customer")
    public ReplacementReport replacementReport(UserInput userInput, Ai ai) {
        return ai.withAutoLlm()
                .withToolObjects(List.of(ruleBook, catalogue, orders))
                .createObject(String.format("""
                        Verify eligibility for replacement of items in the order: %s
                        Check item details from inventory and check whether they are eligible for replacement from rule book.
                        STRICT: Set `isEligible` to true if item is eligible for replacement from rule book.
                        """, userInput.getContent()), ReplacementReport.class);
    }

    @Action(description = "Check stock of items in the oder")
    StockReport stockReport(ReplacementReport replacementReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.STORE_REPRESENTATIVE)
                .withToolObjects(List.of(orders, inventory, catalogue))
                .createObject(String.format("""
                        Check stock for: %s
                        STRICT: Set `hasStock` to true if stock is available.
                        """, replacementReport.text()), StockReport.class);
    }

    @Action
    DeliveryReport deliveryReport(StockReport stockReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.STORE_REPRESENTATIVE)
                .withToolObjects(List.of(inventory, orders))
                .createObject("Confirm delivery details: " + stockReport.text(), DeliveryReport.class);
    }

    @AchievesGoal(description = "Checked for eligibility, stock, delivery schedule")
    @Action
    CustomerConfirmationReport customerConfirmationReport(DeliveryReport deliveryReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.CUSTOMER_REPRESENTATIVE)
                .createObject(String.format("Compose a reply mail to customer: " + deliveryReport.text())
                        .trim(), CustomerConfirmationReport.class);
    }

}
