package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.*;

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
		final StringBuilder html = new StringBuilder("<table><caption>Machine View</caption><tr><th>Machine</th>");
		final List<Machine> allMachines = task.getJobs().getFirst().getMachines();
		final Map<Machine, List<Job>> machineMap = new HashMap<>();
		
		//transform to Machine ordering
		allMachines.sort(null);
		for(Machine machine : allMachines) {
			LinkedList<Job> jobList = new LinkedList<>();
			
			task.getJobs().forEach(job -> job.getScheduledRoute().getStages().stream().filter(stage -> stage.getScheduledMachine().equals(machine)).findFirst().ifPresent(stage -> jobList.add(new Job(job.getName(), job.getDeadline(), new Route(stage.cloned())))));
			
			jobList.sort(Comparator.comparingInt(job -> job.getScheduledRoute().getStages().getFirst().getScheduledMachine().getScheduledTime()));
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
				Machine jobMachine = job.getScheduledRoute().getStages().getFirst().getScheduledMachine();
				if(jobMachine.getScheduledTime() > end)
					html.append("<td colspan=").append(jobMachine.getScheduledTime() - end).append("/>"); //waiting time
				
				html.append("<td bgcolor=" + BG_COLOR + " colspan=").append(jobMachine.getDuration()).append(" >").append(job.getName()).append("</td>"); //running time
				end = jobMachine.getScheduledTime() + jobMachine.getDuration();
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
		final StringBuilder html = new StringBuilder("<table><caption>Job View</caption><tr><th>Job</th><th>Deadline</th>");
		
		//make header
		for(int i = 1; i <= cmax; i++) {
			html.append("<th>").append(String.format("%0" + (int) (Math.log10(cmax) + 1) + "d", i)).append("</th>");
		}
		html.append("</tr>");
		
		//make entries
		for(Job job : task.getJobs()) {
			html.append("<tr><td>").append(job.getName()).append("</td><td>").append(job.getDeadline()).append("</td>");
			
			int end = 0;
			for(Stage stage : job.getScheduledRoute().getStages()) {
				Machine scheduledMachine = stage.getScheduledMachine();
				
				if(scheduledMachine.getScheduledTime() > end)
					html.append("<td colspan=").append(scheduledMachine.getScheduledTime() - end).append("/>"); //waiting time
				
				html.append("<td bgcolor=" + BG_COLOR + " colspan=").append(scheduledMachine.getDuration()).append(" >").append(scheduledMachine.getName()).append("</td>"); //running time
				end = scheduledMachine.getScheduledTime() + scheduledMachine.getDuration();
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
