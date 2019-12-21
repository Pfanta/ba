package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait AlgorithmRepository {

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

    def apply: String = "task.setResult(-1); return task;"
  }

  @combinator object AlgorithmFFS {
    val semanticType: Type = 'Algorithm('FFS)

    def apply: String = "task.setResult(-1); return task;"
  }

  @combinator object AlgorithmOS {
    val semanticType: Type = 'Algorithm('OS)

    def apply: String = "task.setResult(-1); return task;"
  }

  @combinator object Fallback {
    val semanticType: Type = 'Algorithm('NONE)

    def apply: String = "classification.getTask().setResult(-1); return task;"
  }
}