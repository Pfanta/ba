package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.LinkedList;

public class Job {
	@Getter
	private final LinkedList<Stage> stages = new LinkedList<>();
	@Getter
	@Setter
	@NonNull
	private String name;
	@Getter
	@Setter
	private int deadline;

	public Job() {
		this.name = "";
		this.deadline = -1;
	}

	public Job(String name) {
		this.name = name;
		this.deadline = -1;
	}

	public Job(String name, int deadline) {
		this.name = name;
		this.deadline = deadline;
	}

	public void addStage(Stage stage) {
		stages.add(stage);
	}
}
