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

  /*
    @combinator object RANDOM {
      val semanticType: Type = 'Heuristic

      def apply: String =
        s"""|Collections.shuffle(waitingJobsOnMachine);
            |Job jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
    }*/
}