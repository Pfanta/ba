package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Task;
import org.joor.Reflect;

import java.util.List;
import java.util.function.Function;

public class RunnerUtils {
	public static int runResults(Task task, List<String> runners) {
		int min = Integer.MAX_VALUE;
		
		for(String runnerCode : runners) {
			runnerCode = "package org.combinators.cls.scheduling;\n" +
					             "\n" +
					             "import org.combinators.cls.scheduling.model.Task;\n" +
					             "\n" +
					             "public class Runner implements java.util.function.Function<Task, Integer>{\n" +
					             "\tpublic Integer apply(Task task) {\n" +
					             "\t\treturn 404;\n" +
					             "\t}\n" +
					             "}";
			
			Function<Task, Integer> function = Reflect.compile("org.combinators.cls.scheduling.Runner", runnerCode).create().get();
			
			min = Math.min(min, function.apply(task));
		}
		
		return min == Integer.MAX_VALUE ? -1 : min;
	}
	
	
}
