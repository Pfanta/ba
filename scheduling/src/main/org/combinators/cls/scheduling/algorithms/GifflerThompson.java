package org.combinators.cls.scheduling.algorithms;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Stage;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;

import java.util.*;
import java.util.function.Function;


public class GifflerThompson implements Function<ClassificationUtils.Classification, Task> {
    public Task apply(ClassificationUtils.Classification classification) {
        final Task schedule = classification.getTask();
        final Map<Machine, Integer> machineWorkingUntil = new HashMap<>(); //Zi
        final Map<org.combinators.cls.scheduling.model.Job, Integer> jobWorkingUntil = new HashMap<>(); //Rj
        final Map<Job, Integer> stepOfJob = new HashMap<>();
    
        schedule.getAllMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
        schedule.getJobs().forEach(job -> {
            jobWorkingUntil.put(job, 0);
            stepOfJob.put(job, 0);
        });
    
        //Iterate #jobs x #machines times
        Collections.nCopies(classification.getMachineCount() * classification.getJobCount(), 0).forEach(o -> {
            Machine machineToSchedule = null;
            int finishTime = Integer.MAX_VALUE;
        
            //Find machine that may finish first
            for(Job j : schedule.getJobs()) {
                if(stepOfJob.get(j) >= j.getStages().size())
                    continue;
            
                Stage nextStage = j.getStages().get(stepOfJob.get(j));
                int time = nextStage.getDuration() + Math.max(machineWorkingUntil.get(nextStage.getMachine()), jobWorkingUntil.get(j));
                if(time < finishTime) {
                    machineToSchedule = nextStage.getMachine();
                    finishTime = time;
                }
            }
        
            //Find all jobs waiting for machine
            LinkedList<Job> waitingJobsOnMachine = new LinkedList<>();
	        for(Job j : schedule.getJobs()) {
		        if(stepOfJob.get(j) >= j.getStages().size())
			        continue;
		
		        if(j.getStages().get(stepOfJob.get(j)).getMachine().equals(machineToSchedule))
			        waitingJobsOnMachine.add(j);
	        }
	
	        //choose by heuristic
	        //Heuristic
	        //waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getStages().stream().mapToInt(Stage::getDuration).sum()));
	        waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getStages().stream().filter(stage -> j.getStages().indexOf(stage) > stepOfJob.get(j)).mapToInt(Stage::getDuration).sum()));
	        Job jobToSchedule = waitingJobsOnMachine.getLast();
	
	
	        //Schedule Task
	        int jobDuration = jobToSchedule.getStages().get(stepOfJob.get(jobToSchedule)).getDuration();
	        finishTime = jobDuration + Math.max(machineWorkingUntil.get(machineToSchedule), jobWorkingUntil.get(jobToSchedule));
	        jobToSchedule.getStages().get(stepOfJob.get(jobToSchedule)).setScheduledTime(finishTime - jobDuration);
	
	        //Update
	        stepOfJob.put(jobToSchedule, stepOfJob.get(jobToSchedule) + 1);
            jobWorkingUntil.put(jobToSchedule, finishTime);
            machineWorkingUntil.put(machineToSchedule, finishTime);
        });
        return schedule;
    }
}
