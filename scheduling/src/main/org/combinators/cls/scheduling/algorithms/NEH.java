package org.combinators.cls.scheduling.algorithms;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;


public class NEH implements Function<ClassificationUtils.Classification, Task> {
	public Task apply(ClassificationUtils.Classification classification) {
		final LinkedList<Job> jobs = classification.getTask().getJobs();
		
		//sort jobs in descending order
		jobs.sort(Comparator.comparingInt(job -> -job.getRoutes().getFirst().getStages().stream().mapToInt(stage -> stage.getScheduledMachine().getDuration()).sum()));
		
		//initialize local schedule with first job (the job with the longest makespan)
		Task schedule = new Task();
		schedule.add(jobs.remove(0));
		
		//schedule each job in the List
		for(Job job : jobs) {
			int localCmax = Integer.MAX_VALUE;
			Task localTask = new Task();
			
			//add next job at each position
			for(int i = 0; i <= schedule.getJobs().size(); i++) {
				//copy local best
				Task jobList = schedule.cloned();
				jobList.getJobs().add(i, job);
				
				/* ########## FLOWSHOP SCHEDULER MODULE ########## */
				Task localSchedule = jobList.cloned();
				Map<Machine, Integer> machineWorkingUntil = new HashMap<>();
				localSchedule.getJobs().getFirst().getMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
				
				for(Job jobToSchedule : localSchedule.getJobs()) {
					for(int machineIndex = 0; machineIndex < jobToSchedule.getScheduledRoute().getStages().size(); machineIndex++) {
						Machine machine = jobToSchedule.getScheduledRoute().getStages().get(machineIndex).getScheduledMachine();
						
						//finish time of job
						int t1 = machineIndex == 0 ? 0 : jobToSchedule.getScheduledRoute().getStages().get(machineIndex - 1).getScheduledMachine().getFinishTime();
						
						//finish time of machine
						int t2 = machineWorkingUntil.get(machine);
						int scheduleTime = Math.max(t1, t2);
						
						machine.setScheduledTime(scheduleTime);
						machineWorkingUntil.put(machine, machine.getFinishTime());
					}
				}
				/* ########## ########## ########## ########## */
				
				int max = localSchedule.getMakespan();
				if(max < localCmax) {
					localCmax = max;
					localTask = localSchedule;
				}
			}
			
			//update local best
			schedule = localTask;
		}
		schedule.setResult(schedule.getMakespan());
		
		return schedule;
	}
}
