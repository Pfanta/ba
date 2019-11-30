package model;

public enum ShopClass {
	FS,
	FFS,
	JS,
	FJS,
	OS;
	
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
			default:
				throw new IllegalStateException();
		}
	}
}
