package org.combinators.cls.scheduling.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Tuple Class
 * @param <K> Key
 * @param <V> Value
 */
public final class Tuple<K, V> {
	/**
	 * First element / Key
	 */
	@Getter
	@Setter
	private K first;
	
	/**
	 * Second element / Value
	 */
	@Getter
	@Setter
	private V second;
	
	/**
	 Creates a new Tuple
	 @param first First element
	 @param second second element
	 */
	public Tuple(K first, V second) {
		this.first = first;
		this.second = second;
	}
}
