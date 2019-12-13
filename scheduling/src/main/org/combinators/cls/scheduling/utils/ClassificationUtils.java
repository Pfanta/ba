package org.combinators.cls.scheduling.utils;

import lombok.Getter;
import org.combinators.cls.scheduling.model.*;

import java.util.ArrayList;
import java.util.Collection;
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
			return new Classification(-1, -1, false, ShopClass.NONE); //Invalid Task
		
		List<List<Machine>> machines = mapTaskToMachines(task);
		
		int jobCount = machines.size();
		int machineCount = (int) machines.stream().flatMap(Collection::stream).distinct().count();
		
		if(!checkFlexibility(task))
			if(checkIsOpenShop(machines))
				if(checkIsJobShop(task))
					if(checkIsFlowShop(machines))
						return new Classification(machineCount, jobCount, task.hasDeadlines(), ShopClass.FS);
					else
						return new Classification(machineCount, jobCount, task.hasDeadlines(), ShopClass.JS);
				else
					return new Classification(machineCount, jobCount, task.hasDeadlines(), ShopClass.OS);
			else
				return new Classification(machineCount, jobCount, task.hasDeadlines(), ShopClass.NONE);
		else
			return new Classification(machineCount, jobCount, task.hasDeadlines(), ShopClass.FFS); //TODO: Flexible
		
	}
	
	public static JobMachineTuple getTaskDimensions(Task task) {
		List<List<Machine>> machines = mapTaskToMachines(task);
		return new JobMachineTuple(machines.size(), (int) machines.stream().flatMap(Collection::stream).distinct().count());
	}
	
	private static List<List<Machine>> mapTaskToMachines(Task task) {
		return task.getJobs().stream()
				       .map(job -> job.getStages().stream()
						                   .map(stage -> stage.getMachinesWithTimes().stream()
								                                 .map(MachineTuple::getMachine)
								                                 .collect(Collectors.toList()))
						                   .collect(Collectors.toList()).stream()
						                   .flatMap(List::stream)
						                   .collect(Collectors.toList())).collect(Collectors.toList());
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
	 Checks for given Task to be JobShop
	 @param currentTask Task to be classified
	 @return true if model matches
	 @precondition Task is OpenShop
	 */
	private static boolean checkIsJobShop(Task currentTask) {
		return currentTask.getJobs().stream().noneMatch(job -> job.getStages().stream().anyMatch(stage -> stage.getMachinesWithTimes().stream().anyMatch(machineTuple -> machineTuple.getTime().equals(0))));
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
		return currentTask.getJobs().stream().map(Job::getStages).flatMap(List::stream).anyMatch(stage -> stage.getMachinesWithTimes().size() > 1);
	}
	
	/**
	 Validates Task, such that there is min. one Job containing min. one Stage with min. one Machine each
	 @param task Task to be validated
	 @return true for valid Task, false otherwise
	 */
	public static boolean validate(Task task) {
		if(task == null)
			return false;
		
		ArrayList<Job> jobs = task.getJobs();
		if(jobs == null || jobs.size() == 0)
			return false;
		
		for(Job job : jobs) {
			if(job == null)
				return false;
			
			LinkedList<Stage> stages = job.getStages();
			if(stages == null || stages.size() == 0)
				return false;
			
			for(Stage stage : stages) {
				if(stage == null)
					return false;
				
				Collection<MachineTuple> machinesWithTimes = stage.getMachinesWithTimes();
				if(machinesWithTimes == null || machinesWithTimes.size() == 0)
					return false;
				
				for(MachineTuple machineTuple : machinesWithTimes) {
					if(machineTuple == null || machineTuple.getMachine() == null || machineTuple.getTime() == null)
						return false;
				}
			}
		}
		
		return true;
	}
	
	public static class Classification {
		@Getter
		private final int machineCount;
		@Getter
		private final int jobCount;
		@Getter
		private final boolean deadlines;
		@Getter
		private final ShopClass shopClass;

		Classification(int machineCount, int jobCount, boolean deadlines, ShopClass shopClass) {
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
	}
}