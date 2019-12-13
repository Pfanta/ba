package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class LoadSaveUtil {
	
	public static Task load(String file) throws IOException {
		return load(new File(file));
	}
	
	public static Task load(File file) throws IOException {
		Task task = new Task();
		Files.lines(file.toPath()).forEach(s -> task.add(ApplicationUtils.parse(s)));
		
		return task;
	}
	
	public static void save(File file, Task currentTask) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(currentTask.getString());
		writer.close();
	}
}
