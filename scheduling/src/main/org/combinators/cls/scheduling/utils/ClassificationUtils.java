package org.combinators.cls.scheduling.utils;

import lombok.Getter;
import org.combinators.cls.scheduling.model.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassificationUtils {
	
	/**
	 Classifies given Task
	 @param task Task to be classified
	 @return Classification for Task including machine- and job-count
	 */
	public static Classification classify(Task task) {
		if(!validate(task))
			return new Classification(task, -1, -1, false, ShopClass.NONE); //Invalid Task
		
		List<List<Machine>> machines = getMachines(task);
		
		int jobCount = machines.size();
		int machineCount = (int) machines.stream().flatMap(Collection::stream).distinct().count();
		
		if(!checkFlexibility(task))
			if(checkIsOpenShop(machines))
				if(checkIsJobShop(task))
					if(checkIsFlowShop(machines))
						return new Classification(task, machineCount, jobCount, task.hasDeadlines(), ShopClass.FS);
					else
						return new Classification(task, machineCount, jobCount, task.hasDeadlines(), ShopClass.JS);
				else
					return new Classification(task, machineCount, jobCount, task.hasDeadlines(), ShopClass.OS);
			else
				return new Classification(task, machineCount, jobCount, task.hasDeadlines(), ShopClass.NONE);
		else
			return new Classification(task, machineCount, jobCount, task.hasDeadlines(), ShopClass.FFS); //TODO: Flexible
		
	}
	
	public static JobMachineTuple getTaskDimensions(Task task) {
		List<List<Machine>> machines = getMachines(task);
		return new JobMachineTuple(machines.size(), (int) machines.stream().flatMap(Collection::stream).distinct().count());
	}
	
	private static List<List<Machine>> getMachines(Task task) {
		return task.getJobs().stream().map(Job::getMachines).collect(Collectors.toList());
	}
	
	
	/**
	 Checks for given Task to be OpenShop
	 @param machines Machines on current Task
	 @return true if model matches
	 @precondition Task is Non-Flexible
	 */
	private static boolean checkIsOpenShop(List<List<Machine>> machines) {
		return true; //TODO checkIsOpenShop
	}
	
	/**
	 Checks for given Task to be JobShop: Just one route for each job and no duration=0
	 @param currentTask Task to be classified
	 @return true if model matches
	 @precondition Task is OpenShop
	 */
	@SuppressWarnings("RedundantIfStatement")
	private static boolean checkIsJobShop(Task currentTask) {
		if(currentTask.getJobs().stream().anyMatch(job -> job.getRoutes().size() != 1))
			return false;
		
		if(currentTask.getJobs().stream().anyMatch(job -> job.getScheduledRoute().getStages().stream().anyMatch(stage -> stage.getScheduledMachine().getDuration() == 0)))
			return false;
		
		return true;
	}
	
	/**
	 Checks for given Task to be FlowShop
	 @param machineOrders Machines in current Task
	 @return true if model matches
	 @precondition Task is JobShop
	 */
	private static boolean checkIsFlowShop(List<List<Machine>> machineOrders) {
		for(int i = 1; i < machineOrders.size(); i++) {
			if(!machineOrders.get(0).equals(machineOrders.get(i)))
				return false;
		}
		return true;
	}
	
	private static boolean checkFlexibility(Task currentTask) {
		return currentTask.getJobs().stream().anyMatch(job -> job.getRoutes().stream().anyMatch(route -> route.getStages().stream().anyMatch(stage -> stage.getMachines().size() > 1)));
	}
	
	/**
	 Validates Task, such that there is min. one Job containing min. one Stage with min. one Machine each
	 @param task Task to be validated
	 @return true for valid Task, false otherwise
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean validate(Task task) {
		if(task == null)
			return false;
		
		LinkedList<Job> jobs = task.getJobs();
		if(jobs == null || jobs.size() == 0)
			return false;
		
		for(Job job : jobs) {
			if(job == null || job.getName() == null || job.getDeadline() < -1)
				return false;
			
			LinkedList<Route> routes = job.getRoutes();
			if(routes == null || routes.size() == 0)
				return false;
			
			for(Route route : routes) {
				LinkedList<Stage> stages = route.getStages();
				if(stages == null || stages.size() == 0)
					return false;
				
				for(Stage stage : stages) {
					if(stage == null || stage.getMachines().size() == 0)
						return false;
					
					LinkedList<Machine> machines = stage.getMachines();
					
					for(Machine machine : machines) {
						if(machine == null || machine.getName() == null || machine.getDuration() < 0 || machine.getScheduledTime() < -1)
							return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public static Task findBest(LinkedList<Task> schedules) {
		schedules.sort(Comparator.comparingInt(Task::getResult));
		return schedules.getFirst();
	}
	
	public static class Classification implements ICloneable<Classification> {
		@Getter
		private final Task task;
		@Getter
		private final int machineCount;
		@Getter
		private final int jobCount;
		@Getter
		private final boolean deadlines;
		@Getter
		private final ShopClass shopClass;

		Classification(Task task, int machineCount, int jobCount, boolean deadlines, ShopClass shopClass) {
			this.task = task;
			this.machineCount = machineCount;
			this.jobCount = jobCount;
			this.deadlines = deadlines;
			this.shopClass = shopClass;
		}

		/**
		 * @return true for shopClasses FS and FFS
		 */
		public boolean isStrictlyFlowShop() {
			return this.shopClass.equals(ShopClass.FS) || this.shopClass.equals(ShopClass.FFS);
		}

		/**
		 @return true for shopClasses JS and FJS
		 */
		public boolean isStrictlyJobShop() {
			return this.shopClass.equals(ShopClass.JS) || this.shopClass.equals(ShopClass.FJS);
		}

		@Override
		public String toString() {
			return "Classification{" +
					"machineCount=" + machineCount +
					", jobCount=" + jobCount +
					", shopClass=" + shopClass +
					'}';
		}

		@Override
		public Classification cloned() {
			return new Classification(task.cloned(), machineCount, jobCount, deadlines, shopClass);
		}
	}
}