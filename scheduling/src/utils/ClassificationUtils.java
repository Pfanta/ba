package utils;

import lombok.Getter;
import model.Machine;
import model.MachineTuple;
import model.ShopClass;
import model.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassificationUtils {
	public static Classification classify(Task currentTask) {
		int machineCount = getMachineCount(currentTask);
		int jobCount = currentTask.getJobs().size();
		
		if(machineCount < 0)
			return new Classification(-1, jobCount, ShopClass.NONE); //Invalid
		
		
		if(!checkFlexibility(currentTask))
			if(!checkIsOpenShop(currentTask))
				if(!checkIsJobShop(currentTask))
					if(checkIsFlowShop(currentTask))
						return new Classification(machineCount, jobCount, ShopClass.FS);
					else
						return new Classification(machineCount, jobCount, ShopClass.JS);
				else
					return new Classification(machineCount, jobCount, ShopClass.OS);
			else
				return new Classification(machineCount, jobCount, ShopClass.NONE);
		else
			return new Classification(-1, -1, ShopClass.NONE); //TODO: Flexibles
	}
	
	private static boolean checkIsOpenShop(Task currentTask) {
		return false;
	}
	
	private static boolean checkIsJobShop(Task currentTask) {
		return false;
	}
	
	private static boolean checkIsFlowShop(Task currentTask) {
		return false;
	}
	
	private static boolean checkFlexibility(Task currentTask) {
		return false;
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
		
		List<Machine> allMachines = new LinkedList<>();
		machines.forEach(allMachines::addAll);
		
		if(!allMachines.equals(machines.get(0)))
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
	}
}