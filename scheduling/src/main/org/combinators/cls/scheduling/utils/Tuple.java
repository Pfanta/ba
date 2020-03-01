package org.combinators.cls.scheduling.utils;

import lombok.Getter;
import lombok.Setter;

public class Tuple<K, V> {
	@Getter
	@Setter
	K first;
	
	@Getter
	@Setter
	V second;
	
	public Tuple(K first, V second) {
		this.first = first;
		this.second = second;
	}
}
