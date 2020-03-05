package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait HeuristicRepository {
  @combinator object LPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getScheduledRoute().getStages().stream().map(Stage::getScheduledMachine).mapToInt(Machine::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
  }

  @combinator object SPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getScheduledRoute().getStages().stream().map(Stage::getScheduledMachine).mapToInt(Machine::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }


  @combinator object LRPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getScheduledRoute().getStages().stream().filter(stage -> j.getScheduledRoute().getStages().indexOf(stage) >= stepOfJob.get(j)).map(Stage::getScheduledMachine).mapToInt(Machine::getDuration).sum()));
          |Job jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
  }

  @combinator object SRPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort(Comparator.comparingInt(j -> j.getScheduledRoute().getStages().stream().filter(stage -> j.getScheduledRoute().getStages().indexOf(stage) >= stepOfJob.get(j)).map(Stage::getScheduledMachine).mapToInt(Machine::getDuration).sum()));
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
          |				localSchedule.getJobs().getFirst().getMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
          |
          |				for(Job jobToSchedule : localSchedule.getJobs()) {
          |					for(int machineIndex = 0; machineIndex < jobToSchedule.getScheduledRoute().getStages().size(); machineIndex++) {
          |						Machine machine = jobToSchedule.getScheduledRoute().getStages().get(machineIndex).getScheduledMachine();
          |
          |						//finishtime of job
          |						int t1 = machineIndex == 0 ? 0 : jobToSchedule.getScheduledRoute().getStages().get(machineIndex - 1).getScheduledMachine().getFinishTime();
          |
          |						//finishtime of machine
          |						int t2 = machineWorkingUntil.get(machine);
          |						int scheduleTime = Math.max(t1, t2);
          |
          |						machine.setScheduledTime(scheduleTime);
          |						machineWorkingUntil.put(machine, machine.getFinishTime());
          |					}
          |				}
          |""".stripMargin
  }
}