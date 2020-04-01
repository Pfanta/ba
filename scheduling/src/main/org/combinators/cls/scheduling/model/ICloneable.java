package org.combinators.cls.scheduling.model;

/**
 * Interface providing cloning function for all model classes
 *
 * @param <T> Type
 */
public interface ICloneable<T> {
	/**
	 * Deeply clones the object
	 *
	 * @return Deeply cloned object
	 */
	T cloned();
}
