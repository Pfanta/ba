package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.reflection.Reflect;
import org.combinators.cls.scheduling.utils.reflection.ReflectException;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RunnerUtils {
	public static List<Tuple<String, Task>> runResults(ClassificationUtils.Classification classification, Map<String, String> runners, MainWindowAUI callback) {
		List<Tuple<String, Task>> results = new LinkedList<>();
		
		int i = 0;
		for(String heuristic : runners.keySet()) {
			try {
				Function<ClassificationUtils.Classification, Task> function = Reflect.compile("org.combinators.cls.scheduling.Runner" + i, runners.get(heuristic).replace("class Runner", "class Runner" + i)).create().get();
				Task schedule = function.apply(classification.cloned()); //clone to prevent side-effects
				results.add(new Tuple<>(heuristic, schedule));
			} catch(ReflectException e) {
				ApplicationUtils.showException("Reflection exception occurred", "Exception while running results", e);
			}
			callback.onRunnerProgress((float) i / (float) runners.size());
			i++;
		}
		
		results.forEach(t -> System.out.println(t.getFirst() + " : " + t.getSecond().getResult()));
		
		return results;
	}
}
