package model;

import lombok.Getter;
import lombok.Setter;

public class Machine {
	@Getter
	@Setter
	private String name;
	
	Machine(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/*@Override
	public boolean equals(Object other) {
		if(other instanceof Machine)  {
			return ((Machine)other).name.equals(this.name);
		}
		return false;
	}*/
}
