package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait AlgorithmRepository {

  @combinator object GifflerThompson {
    val semanticType: Type = 'Heuristic =>: 'Algorithm('JS)

    def apply(Heuristic: String): String =
      s"""|        final Task schedule = classification.getTask();
          |        final Map<Machine, Integer> machineWorkingUntil = new HashMap<>(); //Zi
          |        final Map<org.combinators.cls.scheduling.model.Job, Integer> jobWorkingUntil = new HashMap<>(); //Rj
          |        final Map<Job, Integer> stepOfJob = new HashMap<>();
          |
          |        schedule.getAllMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
          |        schedule.getJobs().forEach(job -> { jobWorkingUntil.put(job, 0); stepOfJob.put(job, 0); });
          |
          |        //Iterate #jobs x #machines times
          |        Collections.nCopies(classification.getMachineCount() * classification.getJobCount(), 0).forEach(o -> {
          |            Machine machineToSchedule = null;
          |            int finishTime = Integer.MAX_VALUE;
          |
          |            //Find machine that may finish first
          |            for(Job j : schedule.getJobs()) {
          |                if(stepOfJob.get(j) >= j.getStages().size())
          |                    continue;
          |
          |                Stage nextStage = j.getStages().get(stepOfJob.get(j));
          |                int time = nextStage.getDuration() + Math.max(machineWorkingUntil.get(nextStage.getMachine()), jobWorkingUntil.get(j));
          |                if(time < finishTime) {
          |                    machineToSchedule = nextStage.getMachine();
          |                    finishTime = time;
          |                }
          |            }
          |
          |            //Find all jobs waiting for machine
          |            LinkedList<Job> waitingJobsOnMachine = new LinkedList<>();
          |            for (Job j : schedule.getJobs()) {
          |                if(stepOfJob.get(j) >= j.getStages().size())
          |                    continue;
          |
          |                if(j.getStages().get(stepOfJob.get(j)).getMachine().equals(machineToSchedule))
          |                    waitingJobsOnMachine.add(j);
          |            }
          |
          |            //choose by heuristic
          |            $Heuristic
          |
          |            //Schedule Task
          |            int jobDuration = jobToSchedule.getStages().get(stepOfJob.get(jobToSchedule)).getDuration();
          |            finishTime = jobDuration + Math.max(machineWorkingUntil.get(machineToSchedule), jobWorkingUntil.get(jobToSchedule));
          |            jobToSchedule.getStages().get(stepOfJob.get(jobToSchedule)).setScheduledTime(finishTime - jobDuration);
          |
          |            //Update
          |            stepOfJob.put(jobToSchedule, stepOfJob.get(jobToSchedule) + 1);
          |            jobWorkingUntil.put(jobToSchedule, finishTime);
          |            machineWorkingUntil.put(machineToSchedule, finishTime);
          |        });""".stripMargin
  }

  @combinator object AlgorithmFS {
    val semanticType: Type = 'Algorithm('FS)

    def apply: String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }

  @combinator object AlgorithmFFS {
    val semanticType: Type = 'Algorithm('FFS)

    def apply: String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }

  @combinator object AlgorithmOS {
    val semanticType: Type = 'Algorithm('OS)

    def apply: String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }

  @combinator object Fallback {
    val semanticType: Type = 'Algorithm('NONE)

    def apply: String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }
}