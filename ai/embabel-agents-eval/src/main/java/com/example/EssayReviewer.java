package com.example;

import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;

@Agent(description = "Agent that reviews essays and provides feedback")
public class EssayReviewer {

    @Action(description = "Review an essay and provide feedback")
    public EssayReview reviewEssay(Essay essay, OperationContext context) {
        return context.ai()
                .withLlm("gpt-4.1")
                .withSystemPrompt("You are a critical reviewer.")
                .createObject("Review the following essay and provide feedback: " + essay.content() +
                        " For your information, target audience is young programmers. " +
                        " Do not modify the essay, only provide feedback.", EssayReview.class);
    }
}
