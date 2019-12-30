package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait HeuristicRepository {
  @combinator object LPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getStages().stream().mapToInt(Stage::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
  }

  @combinator object SPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getStages().stream().mapToInt(Stage::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }


  @combinator object LRPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getStages().stream().filter(stage -> j.getStages().indexOf(stage) > stepOfJob.get(j)).mapToInt(Stage::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
  }

  @combinator object SRPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getStages().stream().filter(stage -> j.getStages().indexOf(stage) > stepOfJob.get(j)).mapToInt(Stage::getDuration).sum()));
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
      s"""|       /* ########## FLOWSHOP SCHEDULER MODULE ########## */
          |				Task localSchedule = jobList.cloned();
          |				Map<Machine, Integer> machineWorkingUntil = new HashMap<>();
          |				localSchedule.getAllMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
          |
          |				for(Job jobIndex : localSchedule.getJobs()) {
          |					for(int machineIndex = 0; machineIndex < jobIndex.getStages().size(); machineIndex++) {
          |						Stage stage = jobIndex.getStages().get(machineIndex);
          |						int t1 = machineIndex == 0 ? 0 : jobIndex.getStages().get(machineIndex-1).getFinishTime();
          |						int t2 = machineWorkingUntil.get(stage.getMachine());
          |						int scheduleTime = Math.max(t1,t2);
          |						stage.setScheduledTime(scheduleTime);
          |						machineWorkingUntil.put(stage.getMachine(), scheduleTime);
          |					}
          |				}
          |				/* ########## ########## ########## ########## */
          |""".stripMargin
  }

}