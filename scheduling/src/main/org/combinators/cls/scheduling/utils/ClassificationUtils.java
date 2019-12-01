package org.combinators.cls.scheduling.utils;

import lombok.Getter;
import org.combinators.cls.scheduling.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ClassificationUtils {
	public static Classification classify(Task currentTask) {
		int machineCount = getMachineCount(currentTask);
		int jobCount = currentTask.getJobs().size();
		
		if(machineCount < 0)
			return new Classification(-1, jobCount, ShopClass.NONE); //Invalid
		
		
		if(!checkFlexibility(currentTask))
			if(checkIsOpenShop(currentTask))
				if(checkIsJobShop(currentTask))
					if(checkIsFlowShop(currentTask))
						return new Classification(machineCount, jobCount, ShopClass.FS);
					else
						return new Classification(machineCount, jobCount, ShopClass.JS);
				else
					return new Classification(machineCount, jobCount, ShopClass.OS);
			else
				return new Classification(machineCount, jobCount, ShopClass.NONE);
		else
			return new Classification(-1, -1, ShopClass.NONE); //TODO: Flexible
	}
	
	private static boolean checkIsOpenShop(Task currentTask) {
		return true;//TODO
	}
	
	private static boolean checkIsJobShop(Task currentTask) {
		return true;//TODO
	}
	
	private static boolean checkIsFlowShop(Task currentTask) {
		List<List<Machine>> machineOrders = currentTask.getJobs().stream()
				                                    .map(job -> job.getStages().stream()
						                                                .map(stage -> stage.getMachinesWithTimes().stream()
								                                                              .map(MachineTuple::getMachine)
								                                                              .collect(Collectors.toList()))
						                                                .collect(Collectors.toList()).stream()
						                                                .flatMap(List::stream)
						                                                .collect(Collectors.toList())).collect(Collectors.toList());
		
		for(int i = 1; i < machineOrders.size(); i++) {
			if(!machineOrders.get(0).equals(machineOrders.get(i)))
				return false;
		}
		return true;
	}
	
	private static boolean checkFlexibility(Task currentTask) {
		return currentTask.getJobs().stream().map(Job::getStages).flatMap(List::stream).anyMatch(stage -> stage.getMachinesWithTimes().size() > 1);
	}
	
	private static int getMachineCount(Task currentTask) {
		List<List<Machine>> machines = currentTask.getJobs().stream()
				                               .map(job -> job.getStages().stream()
						                                           .map(stage -> stage.getMachinesWithTimes().stream()
								                                                         .map(MachineTuple::getMachine)
								                                                         .collect(Collectors.toList()))
						                                           .collect(Collectors.toList()).stream()
						                                           .flatMap(List::stream).sorted()
						                                           .collect(Collectors.toList())).collect(Collectors.toList());
		
		HashSet<Machine> allMachines = new HashSet<>();
		machines.forEach(allMachines::addAll);
		
		if(!allMachines.equals(new HashSet<>(machines.get(0))))
			return -1;
		else
			return allMachines.size();
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