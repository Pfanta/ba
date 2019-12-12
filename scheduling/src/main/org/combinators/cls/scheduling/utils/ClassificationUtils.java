package org.combinators.cls.scheduling.utils;

import lombok.Getter;
import org.combinators.cls.scheduling.model.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClassificationUtils {
	
	/**
	 Classifies given Task
	 @param task Task to be classified
	 @return Classification for Task including machine- and job-count
	 */
	public static Classification classify(Task task) {
		List<List<Machine>> machines = mapTaskToMachines(task);
		
		int jobCount = machines.size();
		int machineCount = (int) machines.stream().flatMap(Collection::stream).distinct().count();
		
		if(!checkFlexibility(task))
			if(checkIsOpenShop(machines))
				if(checkIsJobShop(task))
					if(checkIsFlowShop(machines))
						return new Classification(machineCount, jobCount, ShopClass.FS);
					else
						return new Classification(machineCount, jobCount, ShopClass.JS);
				else
					return new Classification(machineCount, jobCount, ShopClass.OS);
			else
				return new Classification(machineCount, jobCount, ShopClass.NONE);
		else
			return new Classification(machineCount, jobCount, ShopClass.FFS); //TODO: Flexible
		
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
		return true; //TODO
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
	
	public static class Classification {
		@Getter
		private int machineCount;
		@Getter
		private int jobCount;
		@Getter
		private ShopClass shopClass;
		
		Classification(int machineCount, int jobCount, ShopClass shopClass) {
			this.machineCount = machineCount;
			this.jobCount = jobCount;
			this.shopClass = shopClass;
		}

		/**
		 @return true for shopClasses FS and FFS
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