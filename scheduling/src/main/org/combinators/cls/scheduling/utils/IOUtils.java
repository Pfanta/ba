package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Stage;
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
		Files.lines(file.toPath()).forEach(s -> task.add(parse(s)));
		
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
	
	/*J1|-1|M1,1,5|M2,1,3|M3,1,3|M4,1,2|*/
	public static Job parse(String input) throws IllegalArgumentException {
		String[] split = input.split("\\|");
		if(split.length < 3)
			throw new IllegalArgumentException("Not a valid String: " + input);
		
		Job job = new Job(split[0], Integer.parseInt(split[1]));
		
		for(int i = 2; i < split.length; i++) {
			String s = split[i];
			String[] machinesSplit = s.split(",");
			
			Stage stage = new Stage(new Machine(machinesSplit[0]), Integer.parseInt(machinesSplit[1]), Integer.parseInt(machinesSplit[2]));
			job.addStage(stage);
		}
		
		return job;
	}
}
