package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.reflection.Reflect;
import org.combinators.cls.scheduling.utils.reflection.ReflectException;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class RunnerUtils {
	public static LinkedList<Task> runResults(ClassificationUtils.Classification classification, List<String> runners, MainWindowAUI callback) {
		LinkedList<Task> results = new LinkedList<>();
		for(int i = 0; i < runners.size(); i++) {
			try {
				Function<ClassificationUtils.Classification, Task> function = Reflect.compile("org.combinators.cls.scheduling.Runner" + i, runners.get(i).replace("class Runner", "class Runner" + i)).create().get();
				Task schedule = function.apply(classification.cloned()); //clone to prevent side-effects
				results.add(schedule);
			} catch(ReflectException e) {
				ApplicationUtils.showException("Reflection exception occurred", "Exception while running results", e);
			}
			callback.onRunnerProgress((float) i / (float) runners.size());
		}
		
		return results;
	}
}
