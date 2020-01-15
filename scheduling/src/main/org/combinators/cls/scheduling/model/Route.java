package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;

public class Route implements ICloneable<Route> {
	@Getter
	LinkedList<Stage> stages;
	
	public Route(Stage... stages) {
		this.stages = new LinkedList<>();
		this.stages.addAll(Arrays.asList(stages));
	}
	
	public Route() {
		this.stages = new LinkedList<>();
	}
	
	public void addStage(Stage stage) {
		this.stages.add(stage);
	}
	
	@Override
	public Route cloned() {
		Route route = new Route();
		this.stages.forEach(stage -> route.getStages().add(stage.cloned()));
		return route;
	}
}
