package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Machine implements Comparable<Machine>, IWritable, ICloneable<Machine> {
	/**
	 Unique name of the machine
	 */
	@Getter
	@Setter
	private String name;
	
	/**
	 Time when Job is scheduled at this machine
	 */
	@Getter
	@Setter
	private int scheduledTime;
	
	/**
	 Duration for Job on this machine
	 */
	@Getter
	@Setter
	private int duration;
	
	public Machine(String name, int duration, int scheduledTime) {
		this.name = name;
		this.duration = duration;
		this.scheduledTime = scheduledTime;
	}
	
	public Machine(String name, int duration) {
		this(name, duration, -1);
	}
	
	public Machine(String name) {
		this(name, -1, -1);
	}
	
	public int getFinishTime() {
		return scheduledTime == -1 ? -1 : scheduledTime + duration;
	}
	
	@Override
	public String getString() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Machine) {
			return ((Machine) other).name.equals(this.name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
	@Override
	public int compareTo(Machine o) {
		return this.name.compareTo(o.name);
	}
	
	@Override
	public Machine cloned() {
		return new Machine(this.name, this.duration, this.scheduledTime);
	}
}
