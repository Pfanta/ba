package org.combinators.cls.scheduling.model;

public enum ShopClass {
	FS,
	FFS,
	JS,
	FJS,
	OS,
	NONE;
	
	@Override
	public String toString() {
		switch(this) {
			case FS:
				return "Flow Shop";
			case FFS:
				return "Flexible Flow Shop";
			case JS:
				return "Job Shop";
			case FJS:
				return "Flexible Job Shop";
			case OS:
				return "Open Shop";
			case NONE:
				return "NONE";
			default:
				throw new IllegalStateException();
		}
	}
}
