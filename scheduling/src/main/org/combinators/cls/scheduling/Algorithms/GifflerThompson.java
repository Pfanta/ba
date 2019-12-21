package org.combinators.cls.scheduling.Algorithms;

import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;

import java.util.function.Function;

public class GifflerThompson implements Function<ClassificationUtils.Classification, Task> {
    public Task apply(ClassificationUtils.Classification classification) {
        /*final Task schedule = classification.getTask();
        final int _machines = classification.getMachineCount();
        final int _jobs = classification.getJobCount();

        final int[] machineWorkingUntil = new int[_machines]; //Zi
        final int[] jobWorkingUntil = new int[_jobs]; //Rj
        final int[] stepOfJob = new int[_jobs];

//        IntStream.range(0, _jobs).forEach(j -> stepOfJob[j]=0);
//        IntStream.range(0, _jobs).forEach(j -> jobWorkingUntil[j]=0);
//        IntStream.range(0, _machines).forEach(i -> machineWorkingUntil[i]=0);

        //Iterate #jobs x #machines times
        Collections.nCopies(_machines * _jobs, 0).forEach(o -> {
            int machineToSchedule = -1;
            int finishTime = Integer.MAX_VALUE;

            //Find machine that may finish first
            for (int jobIndex = 0; jobIndex < _jobs; jobIndex++) {
                int time = schedule.getJobs().get(jobIndex).getStages().get(stepOfJob[jobIndex]).getTime();
                +Math.max(machineWorkingUntil[m], jobWorkingUntil[jobIndex]);
                if (time < finishTime) {
                    machineToSchedule = m;
                    finishTime = time;
                }
            }
            for (int m = 0; m < _machines; m++) {
                for (int j; j < _jobs; j++) {
                    if (stepOfJob[j] < _machines && _order[j][stepOfJob[j]] == m) {
                        int time = _time[j][m] + Math.max(machineWorkingUntil[m], jobWorkingUntil[j]);
                        if (time < finishTime) {
                            machineToSchedule = m;
                            finishTime = time;
                        }
                    }
                }
            }

            //Find all jobs waiting for machine
            LinkedList<Integer> waitingJobsOnMachine = new LinkedList<>();
            for (int j = 0; j < _jobs; j++)
                if (stepOfJob[j] < _machines && _order[j][stepOfJob[j]] == machineToSchedule)
                    waitingJobsOnMachine.add(j);

            //choose by heuristic
            //$Heuristic
            Collections.shuffle(waitingJobsOnMachine);
            Job jobToSchedule = waitingJobsOnMachine.getFirst();

            //Update & Schedule Task
            int jobLength = _time[jobToSchedule][machineToSchedule];
            finishTime = jobLength + Math.max(machineWorkingUntil[machineToSchedule], jobWorkingUntil[jobToSchedule]);
            stepOfJob[jobToSchedule]++;
            jobWorkingUntil[jobToSchedule] = finishTime;
            machineWorkingUntil[machineToSchedule] = finishTime;
            schedule[machineToSchedule].add(new Task(jobToSchedule, jobLength, finishTime - jobLength));
        });*/
        return null;
    }
}
