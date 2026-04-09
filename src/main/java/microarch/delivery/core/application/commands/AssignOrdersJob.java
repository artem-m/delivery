package microarch.delivery.core.application.commands;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignOrdersJob implements Job {
    private final AssignOrdersUseCase useCase;

    @Autowired
    public AssignOrdersJob(AssignOrdersUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void execute(JobExecutionContext context) {
        useCase.handle();
    }
}