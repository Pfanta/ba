package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
	
	public static List<Tuple<Task, Integer>> loadTaillard(File file) throws IOException {
		List<Tuple<Task, Integer>> list = new LinkedList<>();
		
		Files.lines(file.toPath()).forEach(line -> {
			Integer[] values = Arrays.stream(line.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
			
			LinkedList<Job> jobs = new LinkedList<>();
			
			for(int i = 1; i <= 20; i++) {
				Route route = new Route();
				route.addStage(new Stage(new Machine("M1", values[i])));
				route.addStage(new Stage(new Machine("M2", values[i + 20])));
				route.addStage(new Stage(new Machine("M3", values[i + 40])));
				route.addStage(new Stage(new Machine("M4", values[i + 60])));
				route.addStage(new Stage(new Machine("M5", values[i + 80])));
				jobs.add(new Job("J" + i, -1, route));
			}
			
			list.add(new Tuple<>(new Task(jobs), values[0]));
		});
		
		
		return list;
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
	
	/**
	 Parses non-flexible flow- or job-shop from String with format e.g.
	 J1|-1|M1,5|M2,3|M3,3|M4,2|
	 @param input string to parse
	 @return Job
	 @throws IllegalArgumentException IllegalArgumentException
	 */
	public static Job parse(String input) throws IllegalArgumentException {
		String[] split = input.split("\\|");
		if(split.length < 3)
			throw new IllegalArgumentException("Not a valid String: " + input);
		
		
		Job job = new Job(split[0], Integer.parseInt(split[1]));
		Route route = new Route();
		job.addRoute(route);
		
		for(int i = 2; i < split.length; i++) {
			String s = split[i];
			String[] machinesSplit = s.split(",");
			
			Stage stage = new Stage(new Machine(machinesSplit[0], Integer.parseInt(machinesSplit[1])));
			route.addStage(stage);
		}
		
		return job;
	}
}
