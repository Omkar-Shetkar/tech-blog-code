package com.example;

import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;

@Agent(description = "Quality assurance judge for AI outputs")
public class EssayReviewer {

    @Action(description = "Review an essay and provide feedback")
    public EssayReview reviewEssay(Essay essay, OperationContext context) {
        return context.ai()
                .withAutoLlm()
                .withSystemPrompt("You are a critical reviewer.")
                .createObject("Review the following essay and provide feedback: " + essay.content() +
                        " As its addressed to programmers, give specific examples helpful to programmers. " +
                        "Also check for formatting of content for markdown style and logical ordering." +
                        " Do not modify the essay, only provide feedback.", EssayReview.class);
    }
}
