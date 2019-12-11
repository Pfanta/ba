package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

public class MachineTuple {
	@Getter
	private Machine machine;
	@Getter
	@Setter
	private Integer time;

	public MachineTuple(Machine machine, int time) {
		this.machine = machine;
		this.time = time;
	}

	@Override
	public String toString() {
		return machine + "(" + time + ")";
	}
}
