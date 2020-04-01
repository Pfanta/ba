package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Class to model a machine containing the name, the scheduledTime and the duration
 */
public class Machine implements Comparable<Machine>, IWritable, ICloneable<Machine> {
	/**
	 * Unique name of the machine
	 */
	@Getter
	@Setter
	private String name;
	
	/**
	 * Time when Job is scheduled at this machine
	 */
	@Getter
	@Setter
	private int scheduledTime;
	
	/**
	 * Duration for Job on this machine
	 */
	@Getter
	@Setter
	private int duration;
	
	/**
	 * Creates a new machine with given name. Duration and scheduledTime is set to -1
	 *
	 * @param name The machine's name
	 */
	public Machine(String name) {
		this(name, -1, -1);
	}
	
	/**
	 * Creates a new machine with given name and duration. ScheduledTime is set to -1
	 *
	 * @param name The machine's name
	 * @param duration The duration the associated job needs to be processed at this machine
	 */
	public Machine(String name, int duration) {
		this(name, duration, -1);
	}
	
	/**
	 * Creates a new machine with given name, duration and scheduledTime
	 *
	 * @param name The machine's name
	 * @param duration The duration the associated job needs to be processed at this machine
	 * @param scheduledTime The time when the associated job is scheduled on this machine
	 */
	public Machine(String name, int duration, int scheduledTime) {
		this.name = name;
		this.duration = duration;
		this.scheduledTime = scheduledTime;
	}
	
	/**
	 * Returns the time when the machine finishes processing the associated job, which ist the scheduled time plus the duration.
	 *
	 * @return Returns the time when the associated job finishes processing. Returns -1 if the machine has not been scheduled.
	 */
	public int getFinishTime() {
		return scheduledTime == -1 ? -1 : scheduledTime + duration;
	}
	
	/**
	 * Returns a string representation of the machine
	 *
	 * @return String representation
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Returns a string containing all information saved in the object to be saved
	 *
	 * @return String representation
	 */
	@Override
	public String getString() {
		return name;
	}
	
	/**
	 * Deeply clones the object
	 *
	 * @return deeply cloned instance
	 */
	@Override
	public Machine cloned() {
		return new Machine(this.name, this.duration, this.scheduledTime);
	}
	
	/**
	 * Overrides equals method to make machines with equal name equal
	 *
	 * @param other Object to be compared
	 *
	 * @return true if parameter is a machine and has the same name
	 */
	@Override
	public boolean equals(Object other) {
		if(other instanceof Machine) {
			return ((Machine) other).name.equals(this.name);
		}
		return false;
	}
	
	/**
	 * Overrides hashCode method to make machines with equal name equal
	 *
	 * @return The machine's name's hashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
	/**
	 * Compares two machines based on it's name
	 *
	 * @param o The other machine to be compared
	 *
	 * @return Comparison value
	 */
	@Override
	public int compareTo(Machine o) {
		return this.name.compareTo(o.name);
	}
}
