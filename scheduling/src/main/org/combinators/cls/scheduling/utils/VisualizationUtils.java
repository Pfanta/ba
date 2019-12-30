package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Stage;
import org.combinators.cls.scheduling.model.Task;

import java.util.*;

public class VisualizationUtils {
	private final static String BG_COLOR = "#AAAAAA";
	
	/**
	 Transforms given task to HTML string representation
	 @param task task to transform
	 @return HTML string representation
	 */
	public static String taskToHTMLGanttChart(Task task) {
		return "<html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td {padding: 5px;text-align: center;}</style></head><body>" +
				       taskToMachineChart(task) +
				       taskToJobChart(task) +
				       "</body></html>";
	}
	
	private static String taskToMachineChart(Task task) {
		final int cmax = task.getResult();
		final StringBuilder html = new StringBuilder("<table><caption>Scheduling result</caption><tr><th>Machine</th>");
		final List<Machine> allMachines = task.getAllMachines();
		final Map<Machine, List<Job>> machineMap = new HashMap<>();
		
		//transform to Machine ordering
		allMachines.sort(null);
		for(Machine machine : allMachines) {
			LinkedList<Job> jobList = new LinkedList<>();
			
			task.getJobs().forEach(job -> job.getStages().stream().filter(stage -> stage.getMachine().equals(machine)).findFirst().ifPresent(stage -> jobList.add(new Job(job.getName(), job.getDeadline(), stage.cloned()))));
			
			jobList.sort(Comparator.comparingInt(job -> job.getStages().getFirst().getScheduledTime()));
			machineMap.put(machine, jobList);
		}
		
		//make header
		for(int i = 1; i <= cmax; i++) {
			html.append("<th>").append(String.format("%0" + (int) (Math.log10(cmax) + 1) + "d", i)).append("</th>");
		}
		html.append("</tr>");
		
		//make entries
		for(Machine machine : allMachines) {
			List<Job> jobs = machineMap.get(machine);
			html.append("<tr><td>").append(machine.getName()).append("</td>");
			
			int end = 0;
			for(Job job : jobs) {
				Stage stage = job.getStages().getFirst();
				if(stage.getScheduledTime() > end)
					html.append("<td colspan=").append(stage.getScheduledTime() - end).append("/>"); //waiting time
				
				html.append("<td bgcolor=" + BG_COLOR + " colspan=").append(stage.getDuration()).append(" >").append(job.getName()).append("</td>"); //running time
				end = stage.getScheduledTime() + stage.getDuration();
			}
			
			//waiting on end
			if(cmax > end)
				html.append("<td colspan=").append(cmax - end).append("/>");
			
			html.append("</tr>");
		}
		
		html.append("</table>");
		return html.toString();
	}
	
	private static String taskToJobChart(Task task) {
		final int cmax = task.getResult();
		final StringBuilder html = new StringBuilder("<table><caption>Scheduling result</caption><tr><th>Job</th><th>Deadline</th>");
		
		//make header
		for(int i = 1; i <= cmax; i++) {
			html.append("<th>").append(String.format("%0" + (int) (Math.log10(cmax) + 1) + "d", i)).append("</th>");
		}
		html.append("</tr>");
		
		//make entries
		for(Job job : task.getJobs()) {
			html.append("<tr><td>").append(job.getName()).append("</td><td>").append(job.getDeadline()).append("</td>");
			
			int end = 0;
			for(Stage stage : job.getStages()) {
				
				if(stage.getScheduledTime() > end)
					html.append("<td colspan=").append(stage.getScheduledTime() - end).append("/>"); //waiting time
				
				html.append("<td bgcolor=" + BG_COLOR + " colspan=").append(stage.getDuration()).append(" >").append(stage.getMachine().getName()).append("</td>"); //running time
				end = stage.getScheduledTime() + stage.getDuration();
			}
			
			//waiting on end
			if(cmax > end)
				html.append("<td colspan=").append(cmax - end).append("/>");
			
			html.append("</tr>");
		}
		
		html.append("</table>");
		return html.toString();
	}
}
