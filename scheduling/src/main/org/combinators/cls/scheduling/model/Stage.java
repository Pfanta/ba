package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;

public class Stage implements IWritable, ICloneable<Stage> {
	@Getter
	private final LinkedList<Machine> machines;
	
	public Stage(Machine... machines) {
		this.machines = new LinkedList<>();
		this.machines.addAll(Arrays.asList(machines));
	}
	
	public Stage() {
		this.machines = new LinkedList<>();
	}
	
	public Machine getScheduledMachine() {
		return machines.getFirst();
	}
	
	@Override
	public String getString() {
		return /*machines.size() + "x " + */machines.getFirst().getString() + "," + machines.getFirst().getFinishTime();
	}
	
	@Override
	public String toString() {
		return machines.size() + "x " + machines.getFirst() + "(" + machines.getFirst().getDuration() + ")";
	}
	
	@Override
	public Stage cloned() {
		Stage stage = new Stage();
		machines.forEach(machine -> stage.getMachines().add(machine.cloned()));
		return stage;
	}
}
