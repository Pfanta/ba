package org.combinators.cls.scheduling;

import org.combinators.cls.scheduling.Algorithms.GifflerThompson;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlgorithmTest {
	
	@Test
	public void testGTLPT() throws IOException {
		Task task = IOUtils.loadTask(getClass().getResource("/tasks/jobShop4x4.task").getFile());
		Function<ClassificationUtils.Classification, Task> function = new GifflerThompson();
		assertEquals(27, function.apply(ClassificationUtils.classify(task)).getMakespan());
	}
}
