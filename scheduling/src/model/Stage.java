package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

public class Stage {
	@Getter
	private Collection<MachineTuple> machinesWithTimes = new ArrayList<>();
	
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
