package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait HeuristicRepository {
  @combinator object LPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getRoutes().get(0).getStages().stream().mapToInt(Stage::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
  }

  @combinator object SPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getRoutes().get(0).getStages().stream().mapToInt(Stage::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }


  @combinator object LRPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getRoutes().get(0).getStages().stream().filter(stage -> j.getRoutes().get(0).getStages().indexOf(stage) > stepOfJob.get(j)).mapToInt(Stage::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
  }

  @combinator object SRPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getRoutes().get(0).getStages().stream().filter(stage -> j.getRoutes().get(0).getStages().indexOf(stage) > stepOfJob.get(j)).mapToInt(Stage::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }

  @combinator object RANDOM {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|Collections.shuffle(waitingJobsOnMachine);
          |Job jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }

  @combinator object FSScheduler {
    val semanticType: Type = 'FlowShopScheduler

    def apply: String =
      s"""|       Task localSchedule = jobList.cloned();
          |				Map<Machine, Integer> machineWorkingUntil = new HashMap<>();
          |				localSchedule.getAllMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
          |
          |				for(Job jobIndex : localSchedule.getJobs()) {
          |					for(int machineIndex = 0; machineIndex < jobIndex.getRoutes().get(0).getStages().size(); machineIndex++) {
          |						Stage stage = jobIndex.getRoutes().get(0).getStages().get(machineIndex);
          |						int t1 = machineIndex == 0 ? 0 : jobIndex.getRoutes().get(0).getStages().get(machineIndex - 1).getFinishTime();
          |						int t2 = machineWorkingUntil.get(stage.getMachines().get(0));
          |						int scheduleTime = Math.max(t1, t2);
          |						stage.setScheduledTime(scheduleTime);
          |						machineWorkingUntil.put(stage.getMachines().get(0), scheduleTime);
          |					}
          |				}
          |""".stripMargin
  }

}