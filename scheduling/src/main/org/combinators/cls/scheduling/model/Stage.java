package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;

/**
 Class to model a route's stage containing all machines */
public class Stage implements IWritable, ICloneable<Stage> {
	/**
	 All machines of this route
	 */
	@Getter
	private final LinkedList<Machine> machines;
	
	/**
	 Creates an empty stage
	 */
	public Stage() {
		this.machines = new LinkedList<>();
	}
	
	/**
	 Creates a stage with given machines
	 @param machines Machines at this stage
	 */
	public Stage(Machine... machines) {
		this.machines = new LinkedList<>();
		this.machines.addAll(Arrays.asList(machines));
	}
	
	/**
	 Returns the scheduled machine, that is the machine at first position
	 @return Scheduled machine
	 */
	public Machine getScheduledMachine() {
		return machines.getFirst();
	}
	
	/**
	 Returns a string representation of the stage
	 @return String representation
	 */
	@Override
	public String toString() {
		return machines.size() + "x " + machines.getFirst() + "(" + machines.getFirst().getDuration() + ")";
	}
	
	/**
	 Returns a string containing all information saved in the object to be saved
	 @return String representation
	 */
	@Override
	public String getString() {
		return machines.getFirst().getString() + "," + machines.getFirst().getDuration() + "," + machines.getFirst().getFinishTime();
	}
	
	/**
	 Deeply clones the object
	 @return deeply cloned instance
	 */
	@Override
	public Stage cloned() {
		Stage stage = new Stage();
		machines.forEach(machine -> stage.getMachines().add(machine.cloned()));
		return stage;
	}
}
