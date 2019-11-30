package model;

import lombok.Getter;

import java.util.ArrayList;

public class Task {
	
	@Getter
	private ArrayList<Job> jobs;
	
	private Task() {
		jobs = new ArrayList<>();
	}
	
	public static Task empty() {
		return new Task();
	}
	
	public void add(Job job) {
		jobs.add(job);
	}
	
}
