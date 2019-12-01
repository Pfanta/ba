package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public class Job {
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private Integer deadline;
	@Getter
	private LinkedList<Stage> stages;
	
	private Job() {
		stages = new LinkedList<>();
	}
	
	public static Job empty() {
		return new Job();
	}
	
	public void addStage(Stage stage) {
		stages.add(stage);
	}
}
