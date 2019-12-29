package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait HeuristicRepository {

  @combinator object RANDOM {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|Collections.shuffle(waitingJobsOnMachine);
			    |Job jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }

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

  /*
    @combinator object LRPT {
      val semanticType: Type = 'Heuristic

      def apply: String =
        s"""|waitingJobsOnMachine.sort((j1, j2) -> {
            |				int sum1 = 0, sum2 = 0;
            |				for(int m = stepOfJob[j1]; m < _machines; m++)
            |					sum1 += _time[j1][m];
            |
            |			    for(int m = stepOfJob[j2]; m < _machines; m++)
            |				    sum2 += _time[j2][m];
            |
            |				return Integer.compare(sum1, sum2);
            |			});
            |			int jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
    }

    @combinator object SRPT {
      val semanticType: Type = 'Heuristic

      def apply: String =
        s"""|waitingJobsOnMachine.sort((j1, j2) -> {
            |				int sum1 = 0, sum2 = 0;
            |				for(int m = stepOfJob[j1]; m < _machines; m++)
            |					sum1 += _time[j1][m];
            |
            |			    for(int m = stepOfJob[j2]; m < _machines; m++)
            |				    sum2 += _time[j2][m];
            |
            |				return Integer.compare(sum1, sum2);
            |			});
            |			int jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
    }*/

}