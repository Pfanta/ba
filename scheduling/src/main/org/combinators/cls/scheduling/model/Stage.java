package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

public class Stage implements Writable {
	@Getter
	private Collection<MachineTuple> machinesWithTimes = new ArrayList<>();
	
	public Stage(Collection<MachineTuple> machinesWithTimes) {
		this.machinesWithTimes.addAll(machinesWithTimes);
	}
	
	public Stage(MachineTuple machineTuple) {
		this.machinesWithTimes.add(machineTuple);
	}
	
	public Stage() {
	
	}
	
	public void addMachine(Machine machine, int time) {
		machinesWithTimes.add(new MachineTuple(machine, time));
	}
	
	@Override
	public String getString() {
		StringBuilder builder = new StringBuilder();
		machinesWithTimes.forEach(machineTuple -> builder.append(machineTuple.getString()).append(";"));
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}
	
	@Override
	public String toString() {
		if(machinesWithTimes.isEmpty())
			return "";
		
		StringBuilder stringBuilder = new StringBuilder();
		
		machinesWithTimes.forEach(s -> {
			stringBuilder.append(s);
			stringBuilder.append(',');
		});
		stringBuilder.setLength(stringBuilder.length() - 1);
		
		return stringBuilder.toString();
	}
}
