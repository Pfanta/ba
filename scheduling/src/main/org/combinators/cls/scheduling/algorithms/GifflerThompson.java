package org.combinators.cls.scheduling.algorithms;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Stage;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;


public class GifflerThompson implements Function<ClassificationUtils.Classification, Task> {
    public Task apply(ClassificationUtils.Classification classification) {
        final Task schedule = classification.getTask();
        final Map<Machine, Integer> machineWorkingUntil = new HashMap<>(); //Zi
        final Map<org.combinators.cls.scheduling.model.Job, Integer> jobWorkingUntil = new HashMap<>(); //Rj
        final Map<Job, Integer> stepOfJob = new HashMap<>();
    
        schedule.getJobs().getFirst().getMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
	    schedule.getJobs().forEach(job -> {
		    jobWorkingUntil.put(job, 0);
		    stepOfJob.put(job, 0);
	    });
    
        //Iterate #jobs x #machines times
		for (int i = 0; i < classification.getMachineCount() * classification.getJobCount(); i++) {
			Machine machineToSchedule = null;
			int finishTime = Integer.MAX_VALUE;

			//Find machine that may finish first
			for (Job j : schedule.getJobs()) {
				if (stepOfJob.get(j) >= j.getScheduledRoute().getStages().size())
					continue;

				Stage nextStage = j.getScheduledRoute().getStages().get(stepOfJob.get(j));
				int time = nextStage.getScheduledMachine().getDuration() + Math.max(machineWorkingUntil.get(nextStage.getScheduledMachine()), jobWorkingUntil.get(j));
				if (time < finishTime) {
					machineToSchedule = nextStage.getScheduledMachine();
					finishTime = time;
				}
			}
        
            //Find all jobs waiting for machine
	        LinkedList<Job> waitingJobsOnMachine = new LinkedList<>();
	        for(Job j : schedule.getJobs()) {
				if (stepOfJob.get(j) >= j.getScheduledRoute().getStages().size())
					continue;

				if (j.getScheduledRoute().getStages().get(stepOfJob.get(j)).getScheduledMachine().equals(machineToSchedule))
					waitingJobsOnMachine.add(j);
			}

			//choose by heuristic
			waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getScheduledRoute().getStages().stream().map(Stage::getScheduledMachine).mapToInt(Machine::getDuration).sum()));
			Job jobToSchedule = waitingJobsOnMachine.getLast();


			//Schedule Task
			int jobDuration = jobToSchedule.getScheduledRoute().getStages().get(stepOfJob.get(jobToSchedule)).getScheduledMachine().getDuration();
			finishTime = jobDuration + Math.max(machineWorkingUntil.get(machineToSchedule), jobWorkingUntil.get(jobToSchedule));
			jobToSchedule.getScheduledRoute().getStages().get(stepOfJob.get(jobToSchedule)).getScheduledMachine().setScheduledTime(finishTime - jobDuration);

			//Update
			stepOfJob.put(jobToSchedule, stepOfJob.get(jobToSchedule) + 1);
			jobWorkingUntil.put(jobToSchedule, finishTime);
			machineWorkingUntil.put(machineToSchedule, finishTime);
		}
        return schedule;
    }
}
