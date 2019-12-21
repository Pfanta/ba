package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Task;

import java.io.IOException;

public class VisualizationUtils {
    /**
     * Transforms given task to HTML string representation
     *
     * @param task task to transform
     * @param save whether to save the HTML file
     * @return HTML string representation
     */
    public String taskToHTMLGanttChart(Task task, boolean save) {
        String html = taskToHTMLGanttChart(task);

        if (save) {
            try {
                IOUtils.saveHTML(html);
            } catch (IOException ex) {
                ApplicationUtils.showException("Exception while saving.", "Exception was thrown while saving Gantt chart", ex);
            }
        }

        return html;
    }

    private String taskToHTMLGanttChart(Task task) {
        StringBuilder html = new StringBuilder();
        html.append("<html><title>Scheduling result</title><body><table>");

        for (Job job : task.getJobs()) {
        }

        html.append("</table></body></html>");
        return html.toString();
    }
}
