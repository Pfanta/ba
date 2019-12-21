package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.reflection.Reflect;
import org.combinators.cls.scheduling.utils.reflection.ReflectException;

import java.util.List;
import java.util.function.Function;

public class RunnerUtils {
	public static Task runResults(ClassificationUtils.Classification classification, List<String> runners) {
		Task bestSchedule = new Task();
		bestSchedule.setResult(Integer.MAX_VALUE);
		
		for(String runnerCode : runners) {
			try {
				Function<ClassificationUtils.Classification, Task> function = Reflect.compile("org.combinators.cls.scheduling.Runner", runnerCode).create().get();
				Task schedule = function.apply(classification.cloned());
				
				if(schedule.getResult() < bestSchedule.getResult())
					bestSchedule = schedule;
			} catch(ReflectException e) {
				ApplicationUtils.showException("Reflection exception occurred", "Exception while running results", e);
			}
		}
		
		return bestSchedule;
	}
}
