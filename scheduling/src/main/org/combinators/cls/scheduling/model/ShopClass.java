package org.combinators.cls.scheduling.model;

/**
 Enum for all supported shop classes (machine environments) */
public enum ShopClass {
	/**
	 (permutation) Flow Shop
	 */
	FS,
	
	/**
	 Flexible Flow Shop
	 */
	FFS,
	
	/**
	 Job Shop
	 */
	JS,
	
	/**
	 Flexible Job Shop
	 */
	FJS,
	
	/**
	 Open Shop
	 */
	OS,
	
	/**
	 None - not supported
	 */
	NONE;
	
	/**
	 Returns a better readable representation
	 @return Enum value in clear text
	 */
	@Override
	public String toString() {
		switch(this) {
			case FS:
				return "FlowShop";
			case FFS:
				return "FlexibleFlowShop";
			case JS:
				return "JobShop";
			case FJS:
				return "FlexibleJobShop";
			case OS:
				return "OpenShop";
			case NONE:
				return "NONE";
			default:
				throw new IllegalStateException();
		}
	}
}
