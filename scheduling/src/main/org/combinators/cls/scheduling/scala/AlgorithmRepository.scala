package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.syntax._
import org.combinators.cls.types.{Kinding, Type, Variable}

class AlgorithmRepository {

  lazy val shopClass: Variable = Variable("algorithm")

  lazy val kinding: Kinding = Kinding(shopClass)
    .addOption('FS)
    .addOption('FFS)
    .addOption('JS)
    .addOption('FJS)
    .addOption('OS)
    .addOption('NONE)

  @combinator object Scheduler {
    val semanticType: Type = 'Algorithm(shopClass) =>: 'Scheduler(shopClass)

    def apply(Algorithm: String): String =
      s"""|import java.util.*;
			    |import java.util.stream.IntStream;
			    |
			    |public class Scheduler {
			    | private static TaskList[] schedule = new TaskList[_machines];
			    |
			    |	public static void main(String[] args) {
			    |		printInput();
			    |		run();
			    |		printOutput();
			    |	}
			    |
			    | private static void run() {
			    |   $Algorithm
			    | }
			    |
			    | private static void printInput() {
			    |		//IntStream.range(0, _jobs).forEach(i -> System.out.println("Job " + i + ": " + tasks[i].printAsInput()));
			    |	}
			    |
			    |	private static void printOutput() {
			    |		Arrays.stream(schedule).forEach(System.out::println);
			    |	}
			    |
			    | private static class TaskList extends LinkedList<Task> {
			    |		String printAsInput() {
			    |			StringBuilder s = new StringBuilder();
			    |			for(Task task : this) {
			    |				s.append(task.printAsInput()).append(" -> ");
			    |			}
			    |			return s.append("finish").toString();
			    |		}
			    |
			    |		@Override
			    |		public String toString() {
			    |			StringBuilder s = new StringBuilder();
			    |			for(Task task : this) {
			    |				s.append(task.toString()).append(" -> ");
			    |			}
			    |			return s.append("finish").toString();
			    |		}
			    |	}
			    |	private static class Task {
			    |		final int job;
			    |		final int length;
			    |		final int startTime;
			    |
			    |
			    |		Task(int job, int length, int startTime) {
			    |			this.job = job;
			    |			this.length = length;
			    |			this.startTime = startTime;
			    |		}
			    |
			    |		String printAsInput() {
			    |			return "M" + job + "(" + length + ")";
			    |		}
			    |
			    |		@Override
			    |		public String toString() {
			    |			return "| J=" + job + " : t0=" + startTime + " : t=" + length + " |";
			    |		}
			    |	}
			    |}""".stripMargin
  }

  @combinator object GifflerThompson {
    val semanticType: Type = 'Heuristic =>: 'Algorithm('JS)

    def apply(Heuristic: String): String =
      s"""|final int[] machineWorkingUntil = new int[_machines]; //Zi
			    |		final int[] jobWorkingUntil = new int[_jobs]; //Rj
			    |		final int[] stepOfJob = new int[_jobs];
			    |
			    |		IntStream.range(0, _machines).forEach(i -> {
			    |			schedule[i] = new TaskList();
			    |			machineWorkingUntil[i]=0;
			    |		});
			    |		IntStream.range(0, _jobs).forEach(j -> {
			    |			jobWorkingUntil[j]=0;
			    |			stepOfJob[j]=0;
			    |		});
			    |
			    |		//Iterate #jobs x #machines times
			    |		Collections.nCopies(_machines * _jobs, 0).forEach(o -> {
			    |			int machineToSchedule = -1;
			    |			int finishTime = Integer.MAX_VALUE;
			    |
			    |			//Find machine that may finish first
			    |			for(int m = 0; m < _machines; m++) {
			    |				for(int j = 0; j < _jobs; j++) {
			    |					if(stepOfJob[j] < _machines && _order[j][stepOfJob[j]] == m) {
			    |						int time = _time[j][m] + Math.max(machineWorkingUntil[m], jobWorkingUntil[j]);
			    |						if(time < finishTime) {
			    |							machineToSchedule = m;
			    |							finishTime = time;
			    |						}
			    |					}
			    |				}
			    |			}
			    |
			    |			//Find all jobs waiting for machine
			    |			LinkedList<Integer> waitingJobsOnMachine = new LinkedList<>();
			    |			for(int j = 0; j < _jobs; j++)
			    |				if(stepOfJob[j] < _machines && _order[j][stepOfJob[j]] == machineToSchedule)
			    |					waitingJobsOnMachine.add(j);
			    |
			    |			//choose by heuristic
			    |			$Heuristic
			    |
			    |			//Update & Schedule Task
			    |			int jobLength = _time[jobToSchedule][machineToSchedule];
			    |			finishTime = jobLength + Math.max(machineWorkingUntil[machineToSchedule], jobWorkingUntil[jobToSchedule]);
			    |			stepOfJob[jobToSchedule]++;
			    |			jobWorkingUntil[jobToSchedule] = finishTime;
			    |			machineWorkingUntil[machineToSchedule] = finishTime;
			    |			schedule[machineToSchedule].add(new Task(jobToSchedule, jobLength, finishTime-jobLength));
			    |		});""".stripMargin
  }

  @combinator object AlgorithmFS {
    val semanticType: Type = 'Algorithm('FS)

    def apply: String = "System.out.println(\"FS\");"
  }

  @combinator object AlgorithmFFS {
    val semanticType: Type = 'Algorithm('FFS)

    def apply: String = "System.out.println(\"FFS\");"
  }

  @combinator object AlgorithmOS {
    val semanticType: Type = 'Algorithm('OS)

    def apply: String = "System.out.println(\"OS\");"
  }

  @combinator object Fallback {
    val semanticType: Type = 'Algorithm('NONE)

    def apply: String = "System.out.println(\"NONE\");"
  }


  @combinator object RANDOM {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|Collections.shuffle(waitingJobsOnMachine);
			    |			int jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }

  @combinator object LPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort((j1, j2) -> {
			    |				int sum1 = 0, sum2 = 0;
			    |				for(int m = 0; m < _machines; m++) {
			    |					sum1 += _time[j1][m];
			    |					sum2 += _time[j2][m];
			    |  		        }
			    |				return Integer.compare(sum1, sum2);
			    |			});
			    |			int jobToSchedule = waitingJobsOnMachine.getLast();""".stripMargin
  }

  @combinator object SPT {
    val semanticType: Type = 'Heuristic

    def apply: String =
      s"""|waitingJobsOnMachine.sort((j1, j2) -> {
			    |				int sum1 = 0, sum2 = 0;
			    |				for(int m = 0; m < _machines; m++) {
			    |					sum1 += _time[j1][m];
			    |					sum2 += _time[j2][m];
			    |  		        }
			    |				return Integer.compare(sum1, sum2);
			    |			});
			    |			int jobToSchedule = waitingJobsOnMachine.getFirst();""".stripMargin
  }

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
  }
}