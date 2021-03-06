package org.combinators.cls.scheduling.algorithms;

import org.combinators.cls.scheduling.model.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 Test Algorithm */
public class SimpleDispatchingRulesFlowShop {
	public Task apply(Classification classification) {
		Task schedule = classification.getTask();
		LinkedList<Job> waitingJobsOnMachine = schedule.getJobs();
		
		Map<Job, Integer> stepOfJob = new HashMap<>();
		waitingJobsOnMachine.forEach(j -> stepOfJob.put(j, 0));
		
		Task jobList = new Task();
		while(!waitingJobsOnMachine.isEmpty()) {
			/* ########## HEURISTIC ########## */
			waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getScheduledRoute().getStages().stream().filter(stage -> j.getScheduledRoute().getStages().indexOf(stage) > stepOfJob.get(j)).map(Stage::getScheduledMachine).mapToInt(Machine::getDuration).sum()));
			Job jobToSchedule = waitingJobsOnMachine.getFirst();
			/* ########## ########## ########## */
			
			waitingJobsOnMachine.remove(jobToSchedule);
			jobList.add(jobToSchedule);
		}
		
		
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
		schedule = localSchedule;
		return schedule;
	}
}
