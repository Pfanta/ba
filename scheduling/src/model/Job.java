package model;

import lombok.Getter;

import java.util.LinkedList;

public class Job {
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
