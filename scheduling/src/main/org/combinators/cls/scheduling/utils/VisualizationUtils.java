package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Stage;
import org.combinators.cls.scheduling.model.Task;

public class VisualizationUtils {
    /**
     Transforms given task to HTML string representation
     @param task task to transform
     @return HTML string representation
     */
    public static String taskToHTMLGanttChart(Task task) {
        final int cmax = task.getResult();
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td {padding: 5px;text-align: center;}</style></head><body><table><caption>Scheduling result</caption><tr><th>Job</th><th>Deadline</th>");
        
        for(int i = 1; i <= cmax; i++) {
            html.append("<th>").append(String.format("%0" + (int) (Math.log10(cmax) + 1) + "d", i)).append("</th>");
        }
        html.append("</tr>");
        
        for(Job job : task.getJobs()) {
            html.append("<tr><td>").append(job.getName()).append("</td><td>").append(job.getDeadline()).append("</td>");
            
            int end = 0;
            for(Stage stage : job.getStages()) {
                
                if(stage.getScheduledTime() > end)
                    html.append("<td colspan=").append(stage.getScheduledTime() - end).append("/>"); //waiting time
                
                html.append("<td colspan=").append(stage.getTime()).append(" >").append(stage.getMachine().getName()).append("</td>"); //running time
                end = stage.getScheduledTime() + stage.getTime();
            }
            
            //waiting on end
            if(cmax > end)
                html.append("<td colspan=").append(cmax - end).append("/>");
            
            html.append("</tr>");
        }
        
        html.append("</table></body></html>");
        return html.toString();
    }
}
