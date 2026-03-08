package com.example;

import com.embabel.agent.api.invocation.SupervisorInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.domain.io.UserInput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluationController {

    private final AgentPlatform agentPlatform;

    public EvaluationController(AgentPlatform agentPlatform) {
        this.agentPlatform = agentPlatform;
    }

    @GetMapping("/evaluate")
    public Essay evaluate(String query) {
        return SupervisorInvocation.on(agentPlatform)
                .returning(Essay.class)
                .invoke(new UserInput(query));

    }
}
