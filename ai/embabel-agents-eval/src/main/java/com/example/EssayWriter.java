package com.example;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;

@Agent(description = "Quality assurance judge for AI outputs")
class EssayWriter {

    @Action(description = "Write an essay based on user input")
    Essay writeEssay(UserInput userInput, OperationContext context) {
        return context.ai()
                .withAutoLlm()
                .withSystemPrompt("You are a creative writer.")
                .createObject("Write an essay based on the following user input: " + userInput.getContent(), Essay.class);
    }

    @Action(description = "Improve an essay based on a review")
    @AchievesGoal(description = "Produce an improved essay based on the review")
    Essay writeEssay(Essay essay, EssayReview review, OperationContext context) {
        return context.ai()
                .withAutoLlm()
                .withSystemPrompt("You are a creative writer. Use the following review to improve the essay.")
                .createObject("Improve the following essay based on this review: " + essay.content() + "\nReview: " + review.review(), Essay.class);
    }
}

