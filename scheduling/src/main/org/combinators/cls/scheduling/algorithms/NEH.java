package org.combinators.cls.scheduling.algorithms;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Stage;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class NEH implements Function<ClassificationUtils.Classification, Task> {
	public Task apply(ClassificationUtils.Classification classification) {
		final List<Job> jobs = classification.getTask().getJobs();
		
		//sort jobs in descending order
		jobs.sort(Comparator.comparingInt(job -> -job.getRoutes().get(0).getStages().stream().mapToInt(Stage::getDuration).sum()));
		
		Task schedule = new Task();
		schedule.add(jobs.remove(0));
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
				localSchedule.getAllMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
				
				for(Job jobIndex : localSchedule.getJobs()) {
					for(int machineIndex = 0; machineIndex < jobIndex.getRoutes().get(0).getStages().size(); machineIndex++) {
						Stage stage = jobIndex.getRoutes().get(0).getStages().get(machineIndex);
						int t1 = machineIndex == 0 ? 0 : jobIndex.getRoutes().get(0).getStages().get(machineIndex - 1).getFinishTime();
						int t2 = machineWorkingUntil.get(stage.getMachines().get(0));
						int scheduleTime = Math.max(t1, t2);
						stage.setScheduledTime(scheduleTime);
						machineWorkingUntil.put(stage.getMachines().get(0), scheduleTime);
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
