package org.combinators.cls.scheduling.algorithms;

import org.combinators.cls.scheduling.model.Classification;
import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 Test Algorithm */
public class RandomPermutationFlowShop {
	public Task apply(Classification classification) {
		Task schedule = classification.getTask();
		
		Task localBest = new Task();
		localBest.setResult(Integer.MAX_VALUE);
		Task jobList = new Task(schedule.getJobs());
		for(int i = 0; i < 50000; i++) {
			Collections.shuffle(jobList.getJobs());
			
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
			
			localSchedule.setResult(localSchedule.getMakespan());
			if(localSchedule.getResult() < localBest.getResult())
				localBest = localSchedule;
		}
		schedule = localBest;
		return schedule;
	}
}
