package microarch.delivery.core.application.commands;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveCouriersJob implements Job {
    private final MoveCouriersUseCase useCase;

    @Autowired
    public MoveCouriersJob(MoveCouriersUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void execute(JobExecutionContext context) {
        useCase.handle();
    }
}