package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait AlgorithmRepository extends RunnerRepository {

  @combinator object NEH {
    val semanticType: Type = 'FlowShopScheduler =>: 'Algorithm :&: problemClass.flowShop

    def apply(FSScheduler: String): String =
      s"""|   final LinkedList<Job> jobs = classification.getTask().getJobs();
          |
          |		//sort jobs in descending order
          |		jobs.sort(Comparator.comparingInt(job -> -job.getScheduledRoute().getStages().stream().mapToInt(stage -> stage.getScheduledMachine().getDuration()).sum()));
          |
          |		//initialize local schedule with first job (the job with the longest makespan)
          |		Task schedule = new Task();
          |		schedule.add(jobs.remove(0));
          |
          |		//schedule each job in the List
          |		for(Job job : jobs) {
          |			int localCmax = Integer.MAX_VALUE;
          |			Task localTask = new Task();
          |
          |			//add next job at each position
          |			for(int i = 0; i <= schedule.getJobs().size(); i++) {
          |				//copy local best
          |				Task jobList = schedule.cloned();
          |				jobList.getJobs().add(i, job);
          |
          |				$FSScheduler
          |
          |				int max = localSchedule.getMakespan();
          |				if(max < localCmax) {
          |					localCmax = max;
          |					localTask = localSchedule;
          |				}
          |			}
          |
          |			//update local best
          |			schedule = localTask;
          |		}
          |""".stripMargin
  }

  @combinator object SimpleDispatchingRulesFlowShop {
    val semanticType: Type = 'Heuristic =>: 'FlowShopScheduler =>: 'Algorithm :&: problemClass.flowShop

    def apply(Heuristic: String, FSScheduler: String): String =
      s"""|   Task schedule = classification.getTask();
          |		LinkedList<Job> waitingJobsOnMachine = schedule.getJobs();
          |
          |   Map<Job, Integer> stepOfJob = new HashMap<>();
          |		waitingJobsOnMachine.forEach(j -> stepOfJob.put(j,0));
          |
          |		Task jobList = new Task();
          |   while(!waitingJobsOnMachine.isEmpty()) {
          |     $Heuristic
          |     waitingJobsOnMachine.remove(jobToSchedule);
          |			jobList.add(jobToSchedule);
          |   }
          |
          |   $FSScheduler
          |
          |   schedule = localSchedule;""".stripMargin
  }

  @combinator object RandomPermutationFlowShop {
    val semanticType: Type = 'FlowShopScheduler =>: 'Algorithm :&: problemClass.flowShop

    def apply(FSScheduler: String): String =
      s"""|   Task schedule = classification.getTask();
          |
          |		Task localBest = new Task();
          |		localBest.setResult(Integer.MAX_VALUE);
          |		Task jobList = new Task(schedule.getJobs());
          |		for(int i = 0; i < 50000; i++) {
          |			Collections.shuffle(jobList.getJobs());
          |
          |			$FSScheduler
          |
          |			localSchedule.setResult(localSchedule.getMakespan());
          |			if(localSchedule.getResult() < localBest.getResult())
          |				localBest = localSchedule;
          |		}
          |		schedule = localBest;""".stripMargin
  }

  @combinator object GifflerThompson {
    val semanticType: Type = 'Heuristic =>: 'Algorithm :&: problemClass.jobShop

    def apply(Heuristic: String): String =
      s"""|        final Task schedule = classification.getTask();
          |        final Map<Machine, Integer> machineWorkingUntil = new HashMap<>(); //Zi
          |        final Map<org.combinators.cls.scheduling.model.Job, Integer> jobWorkingUntil = new HashMap<>(); //Rj
          |        final Map<Job, Integer> stepOfJob = new HashMap<>();
          |
          |        schedule.getJobs().getFirst().getMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
          |        schedule.getJobs().forEach(job -> {
          |            jobWorkingUntil.put(job, 0);
          |            stepOfJob.put(job, 0);
          |        });
          |
          |        //Iterate #jobs x #machines times
          |        for (int i = 0; i < classification.getMachineCount() * classification.getJobCount(); i++) {
          |            Machine machineToSchedule = null;
          |            int finishTime = Integer.MAX_VALUE;
          |
          |            //Find machine that may finish first
          |            for(Job j : schedule.getJobs()) {
          |	            if(stepOfJob.get(j) >= j.getScheduledRoute().getStages().size())
          |		            continue;
          |
          |	            Stage nextStage = j.getScheduledRoute().getStages().get(stepOfJob.get(j));
          |	            int time = nextStage.getScheduledMachine().getDuration() + Math.max(machineWorkingUntil.get(nextStage.getMachines().get(0)), jobWorkingUntil.get(j));
          |	            if(time < finishTime) {
          |		            machineToSchedule = nextStage.getMachines().get(0);
          |		            finishTime = time;
          |	            }
          |            }
          |
          |            //Find all jobs waiting for machine
          |            LinkedList<Job> waitingJobsOnMachine = new LinkedList<>();
          |	        for(Job j : schedule.getJobs()) {
          |		        if(stepOfJob.get(j) >= j.getScheduledRoute().getStages().size())
          |			        continue;
          |
          |		        if(j.getScheduledRoute().getStages().get(stepOfJob.get(j)).getMachines().get(0).equals(machineToSchedule))
          |			        waitingJobsOnMachine.add(j);
          |	        }
          |
          |	        //choose by heuristic
          |	        $Heuristic
          |
          |	        int jobDuration = jobToSchedule.getScheduledRoute().getStages().get(stepOfJob.get(jobToSchedule)).getScheduledMachine().getDuration();
          |	        finishTime = jobDuration + Math.max(machineWorkingUntil.get(machineToSchedule), jobWorkingUntil.get(jobToSchedule));
          |	        jobToSchedule.getScheduledRoute().getStages().get(stepOfJob.get(jobToSchedule)).getScheduledMachine().setScheduledTime(finishTime - jobDuration);
          |
          |	        //Update
          |	        stepOfJob.put(jobToSchedule, stepOfJob.get(jobToSchedule) + 1);
          |	        jobWorkingUntil.put(jobToSchedule, finishTime);
          |	        machineWorkingUntil.put(machineToSchedule, finishTime);
          |        }""".stripMargin
  }

  @combinator object AlgorithmFFS {
    val semanticType: Type = 'Algorithm :&: problemClass.flexibleFlowShop

    def apply: String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }

  @combinator object AlgorithmOS {
    val semanticType: Type = 'Algorithm :&: problemClass.flexibleJobShop

    def apply: String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }

  @combinator object Fallback {
    val semanticType: Type = 'Algorithm :&: problemClass.none

    def apply: String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }
}