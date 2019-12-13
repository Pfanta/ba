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
