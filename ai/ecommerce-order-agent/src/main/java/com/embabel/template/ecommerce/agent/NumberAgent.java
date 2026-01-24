package com.embabel.template.ecommerce.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.annotation.Condition;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Agent(description = "Agent for odd and even numbers")
public class NumberAgent {

    static final Logger logger = LoggerFactory.getLogger(NumberAgent.class);

    public record NumberReport(boolean isEven) {
    }

    public record EvenNumberReport(String content) {
    }

    public record OddNumberReport(String content) {
    }

    @Action
    public NumberReport readUserInput(UserInput userInput, Ai ai, OperationContext operationContext) {
        NumberReport numberReport = ai.withAutoLlm()
                .createObject("Verify whether the number is even or not: " + userInput.getContent(), NumberReport.class);
        operationContext.set("isEven", numberReport.isEven());
        return numberReport;
    }

    @AchievesGoal(description = "Report about even number")
    @Action(pre = {"isEven"})
    public EvenNumberReport evenNumberReport(NumberReport numberReport, OperationContext context) {
        return context.ai()
                .withAutoLlm()
                .createObject("Describe the even number in the report: " + numberReport, EvenNumberReport.class);
    }

    @AchievesGoal(description = "Report about odd number")
    @Action
    public OddNumberReport oddNumberReport(NumberReport numberReport, OperationContext context) {
        return context.ai()
                .withAutoLlm()
                .createObject("Describe the odd number in the report: " + numberReport, OddNumberReport.class);
    }


    @Condition
    public boolean isEven(NumberReport numberReport) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info("Check if the number is even or not: {}", numberReport);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return numberReport.isEven();
    }

//    @Condition
//    public boolean isOdd(NumberReport numberReport) {
//        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        logger.info("Check if the number is even or not: {}", numberReport);
//        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        return !numberReport.isEven();
//    }


}
