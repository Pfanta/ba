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
import java.util.stream.Collectors;

/**
 * Utils for loading and saving
 */
public class IOUtils {
	
	/**
	 * Loads a task
	 *
	 * @param file File to be loaded
	 *
	 * @return Loaded task
	 *
	 * @throws IOException If the file is not readable
	 */
	public static Task loadTask(File file) throws IOException {
		Task task = new Task();
		Files.lines(file.toPath()).forEach(s -> task.add(parse(s)));
		
		return task;
	}
	
	/**
	 * Loads a taillard instance file
	 *
	 * @param file File to be loaded
	 *
	 * @return Loaded tasks and upper bounds
	 *
	 * @throws IOException If the file is not readable
	 */
	public static List<Tuple<Task, Integer>> loadTaillard(File file) throws IOException {
		List<Tuple<Task, Integer>> list = new LinkedList<>();
		
		List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList());
		int[] opt = Arrays.stream(lines.get(0).split(",")).mapToInt(Integer::parseInt).toArray();
		int numJobs = (lines.size() - 11) / 10;
		
		for(int instance = 0; instance < 10; instance++) {
			int[][] values = lines.subList(instance * (numJobs + 1) + 2, instance * (numJobs + 1) + 2 + numJobs).stream().map(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
			
			LinkedList<Job> jobs = new LinkedList<>();
			
			for(int job = 0; job < values[3].length; job++) {
				Route route = new Route();
				for(int machine = 0; machine < values.length; machine++) {
					route.addStage(new Stage(new Machine("M" + machine, values[machine][job])));
				}
				jobs.add(new Job("J" + job, -1, 0, route));
			}
			
			list.add(new Tuple<>(new Task(jobs), opt[instance]));
		}
		
		
		return list;
	}
	
	/**
	 * Saves a task to a fle
	 *
	 * @param file File to create or override
	 *
	 * @param currentTask Task to be saved
	 *
	 * @throws IOException If the file is not readable
	 */
	public static void saveTask(File file, Task currentTask) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(currentTask.getString());
		writer.close();
	}
	
	/**
	 * Parses non-flexible flow- or job-shop from String with format e.g.
	 * J1|-1|M1,5|M2,3|M3,3|M4,2|
	 *
	 * @param input string to parse
	 *
	 * @return Job
	 *
	 * @throws IllegalArgumentException IllegalArgumentException
	 */
	public static Job parse(String input) throws IllegalArgumentException {
		String[] split = input.split("\\|");
		if(split.length < 3)
			throw new IllegalArgumentException("Not a valid String: " + input);
		
		
		Job job = new Job(split[0], Integer.parseInt(split[1]), 0);
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
