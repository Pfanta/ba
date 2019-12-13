package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Machine implements Comparable<Machine>, IWritable, ICloneable<Machine> {
	@Getter
	@Setter
	private String name;
	
	public Machine(String name) {
		this.name = name;
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
		return new Machine(this.name);
	}
}
