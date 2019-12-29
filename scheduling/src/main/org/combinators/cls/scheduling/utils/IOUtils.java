package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class IOUtils {

	private static final String HTML_LOCATION = "C:\\";

	public static Task loadTask(String file) throws IOException {
		return loadTask(new File(file));
	}

	public static Task loadTask(File file) throws IOException {
		Task task = new Task();
		Files.lines(file.toPath()).forEach(s -> task.add(ApplicationUtils.parse(s)));

		return task;
	}

	public static void saveTask(File file, Task currentTask) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(currentTask.getString());
		writer.close();
	}

	public static void saveHTML(String html) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(HTML_LOCATION.concat("result.html"))));
		writer.write(html);
		writer.close();
	}
}
