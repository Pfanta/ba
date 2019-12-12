package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

public class JobMachineTuple {
	@Getter
	@Setter
	int jobCount;
	@Getter
	@Setter
	int machineCount;
	
	public JobMachineTuple(int jobCount, int machineCount) {
		this.jobCount = jobCount;
		this.machineCount = machineCount;
	}
}
