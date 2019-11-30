package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Task {
	
	@Getter
	private ArrayList<Job> jobs;
	private Collection<Machine> allMachines;
	
	private Task() {
		jobs = new ArrayList<>();
		allMachines = new HashSet<>();
	}
	
	public static Task empty() {
		return new Task();
	}
	
	public void add(Job job) {
		jobs.add(job);
	}
	
}
