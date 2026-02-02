package com.example.multiagent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.api.invocation.AgentInvocation;
import com.embabel.agent.api.invocation.SupervisorInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.domain.io.UserInput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MultiagentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiagentApplication.class, args);
    }

}


@RestController
class CustomerController {

    private final AgentPlatform agentPlatform;

    public CustomerController(AgentPlatform agentPlatform) {
        this.agentPlatform = agentPlatform;
    }

    @GetMapping("/api")
    public SummarisedQuery query(@RequestParam("query") String query) {
        return SupervisorInvocation.on(agentPlatform)
                .returning(SummarisedQuery.class)
                .invoke(new UserInput(query));
    }
}

record TranslatedQuery(String query) {
}

record SummarisedQuery(String orderId, String query) {
}

@Agent(description = "This agent translates customer queries to English.")
class TranslationAgent {

    @Action
    @AchievesGoal(description = "Translate customer query to English")
    public TranslatedQuery translateToEnglish(UserInput userInput, OperationContext operationContext) {
        return operationContext.ai()
                .withAutoLlm()
                .createObject(String.format("""
                        Translate the following text to English: {%s}
                        Output only the translated text.
                        """, userInput.getContent()), TranslatedQuery.class);
    }
}

@Agent(description = "This agent summarizes customer queries.")
class SummarizationAgent {

    @Action
    @AchievesGoal(description = "Summarize translated customer query for order processing")
    public SummarisedQuery summarisedQuery(TranslatedQuery translatedQuery, OperationContext operationContext) {
        return operationContext.ai()
                .withAutoLlm()
                .createObject(String.format("""
                        Summarize the following customer query into a concise summary suitable for order processing: {%s}
                        """, translatedQuery.query()), SummarisedQuery.class);

    }

}