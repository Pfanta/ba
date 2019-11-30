package utils;

import lombok.Getter;
import model.ShopClass;
import model.Task;

public class ClassificationUtils {
	public static Classification classify(Task currentTask) {
		return new Classification(0, 0, ShopClass.OS);
	}
	
	public static class Classification {
		@Getter
		private int machineCount;
		@Getter
		private int jobCount;
		@Getter
		private ShopClass shopClass;
		
		Classification(int machineCount, int jobCount, ShopClass shopClass) {
			this.machineCount = machineCount;
			this.jobCount = jobCount;
			this.shopClass = shopClass;
		}
	}
}