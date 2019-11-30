package scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.syntax._
import org.combinators.cls.types.{Kinding, Type, Variable}

class AlgorithmRepository {

  lazy val algorithm = Variable("algorithm")

  lazy val kinding: Kinding = Kinding(algorithm).addOption('true).addOption('false)

  @combinator object Scheduler {
    val semanticType: Type = 'Task =>: 'Algorithm(algorithm) =>: 'Scheduler(algorithm)

    def apply(Task: String, Algorithm: String): String =
      s"""|import java.util.*;
			    |import java.util.stream.IntStream;
			    |
			    |public class Scheduler {
			    |  $Task
			    |
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

  @combinator object Task1 {
    val semanticType: Type = 'Task

    def apply(): String =
      s"""|  private static int _jobs = 4;
			    |	private static int _machines = 4;
			    |
			    |	private static int[][] _time = new int[][]{
			    |			{5,3,3,2},
			    |			{7,4,8,6},
			    |			{1,6,5,3},
			    |			{2,1,4,7}
			    |	};
			    |
			    |	private static int[][] _order = new int[][]{
			    |			{0,1,2,3},
			    |			{1,0,2,3},
			    |			{3,2,1,0},
			    |			{2,3,1,0}
			    |	};""".stripMargin
  }

  @combinator object Algorithm0 {
    val semanticType: Type = 'Heuristic =>: 'Algorithm('false)

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

  @combinator object Algorithm1 {
    val semanticType: Type = 'Algorithm('true)

    def apply: String = "System.out.println(\"Hello World\");"
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

  //region old
  /*
  lazy val loopFlag = Variable("loopFlag") //Done
  lazy val saveUserDataFlag = Variable("saveUserDataFlag") //Done
  //lazy val isLowerCaseFlag = Variable("isLowerCaseFlag")
  //lazy val ask4NameFlag = Variable("ask4NameFlag")
  //lazy val ask4LandFlag = Variable("ask4LandFlag")
  //lazy val ask4StreetFlag = Variable("ask4StreetFlag")
  //lazy val ask4ZipFlag = Variable("ask4ZipFlag")
  //lazy val ask4CityFlag = Variable("ask4CityFlag")

  lazy val kinding1: Kinding =
    Kinding(loopFlag).addOption('true).addOption('false)
  lazy val kinding2: Kinding =
    Kinding(saveUserDataFlag).addOption('true).addOption('false)
  // lazy val kinding3: Kinding =
  //   Kinding(isLowerCaseFlag).addOption('true).addOption('false)
  //lazy val kinding4: Kinding =
  //  Kinding(ask4NameFlag).addOption('true).addOption('false)
  //lazy val kinding5: Kinding =
  //  Kinding(ask4LandFlag).addOption('true).addOption('false)
  //lazy val kinding6: Kinding =
  //   Kinding(ask4StreetFlag).addOption('true).addOption('false)
  // lazy val kinding7: Kinding =
  //  Kinding(ask4ZipFlag).addOption('true).addOption('false)
  //lazy val kinding8: Kinding =
  // Kinding(ask4CityFlag).addOption('true).addOption('false)

  @combinator object Chatbot {
    def apply(greeting : String,
        problemdetectionandsolution : String,
        addressrecognition: String,
        conclusion: String,
        helpMethods: String): String =
    s"""
       |import java.io.BufferedWriter;
       |import java.io.FileWriter;
       |import java.io.IOException;
       |import java.text.SimpleDateFormat;
       |import java.util.ArrayList;
       |import java.util.Date;
       |import java.util.HashMap;
       |import java.util.HashSet;
       |import java.util.Scanner;
       |import java.util.Set;
       |import java.util.Stack;
       |import org.apache.commons.lang3.StringUtils;
       |
       |class Chatbot{
       |	private static Boolean knowingAddress = false;
       |	private static Scanner sc = new Scanner(System.in);
       |	private static Stack<String> lastRules = new Stack<>();
       |	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
       |	private static Date date = new Date();
       |	private static String savedUserData ="";
       |	public static void main(String[] args){
       |		savedUserData+="Start Chatbot "+sdf.format(date)+", Drücken Sie :b um einen Schritt zurück zu kommen (Außer bei Ja/Nein Fragen)."+System.lineSeparator();
       |		savedUserData+="---------------------------------------------------------------------------------------------------------------"+System.lineSeparator();
       |    System.out.println("Start Chatbot "+sdf.format(date)+", Drücken Sie :b um einen Schritt zurück zu kommen (Außer bei Ja/Nein Fragen).");
       |    System.out.println("---------------------------------------------------------------------------------------------------------------");
       |		lastRules.push("StartID");
       |		findMatchingRule("StartID");
       |	}
       |	private static void findMatchingRule(String currentID){
       |		String nextLine="";
       |		HashMap<String, String> tRule;
       |		switch (currentID){
       |      $greeting
       |			$problemdetectionandsolution
       |      $addressrecognition
       |      $conclusion
       |		}
       |	}
       |$helpMethods
       |}
    """.stripMargin
    val semanticType: Type = 'Greeting =>: 'ProblemdetectionAndSolution =>: 'Addressrecognition =>: 'Conclusion(loopFlag, saveUserDataFlag) =>: 'HelpMethods =>: 'Chatbot(loopFlag, saveUserDataFlag)
  }

  @combinator object Greeting {
    def apply: String =
    s"""
       |			case "StartID":
       |						savedUserData+="Chatbot:Willkommen, welches Ihrer bestellten Produkte ist defekt?"+System.lineSeparator();
       |						System.out.println("Willkommen, welches Ihrer bestellten Produkte ist defekt?");
       |						findMatchingRule("M_rj6c4MAmEemBZcpC9gfPbA");
       |						break;
       |			case "M_rj6c4MAmEemBZcpC9gfPbA":
       |						if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |			tRule.put("Kühlschrank","T_wKSLEMAmEemBZcpC9gfPbA");
       |			tRule.put("Kühltruhe","T_wKSLEMAmEemBZcpC9gfPbA");
       |			tRule.put("Mikrowelle","T_xL4K8cAmEemBZcpC9gfPbA");
       |			tRule.put("Ofen","T__nanccAmEemBZcpC9gfPbA");
       |			tRule.put("Herd","T__nanccAmEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'Greeting
  }


  @combinator object ProblemdetectionAndSolution {
    def apply(
         chatbotText_wKSLEMAmEemBZcpC9gfPbA : String, chatbotMatchingWords_wKSLEMAmEemBZcpC9gfPbA : String,
         chatbotText_xL4K8cAmEemBZcpC9gfPbA : String, chatbotMatchingWords_xL4K8cAmEemBZcpC9gfPbA : String,
         chatbotText__nanccAmEemBZcpC9gfPbA : String, chatbotMatchingWords__nanccAmEemBZcpC9gfPbA : String,
         chatbotText_JBWyUcAnEemBZcpC9gfPbA : String, chatbotMatchingWords_JBWyUcAnEemBZcpC9gfPbA : String,
         chatbotText_JoBJscAnEemBZcpC9gfPbA : String, chatbotMatchingWords_JoBJscAnEemBZcpC9gfPbA : String,
         chatbotText_Kr62scAnEemBZcpC9gfPbA : String, chatbotMatchingWords_Kr62scAnEemBZcpC9gfPbA : String,
         chatbotText_Ln590cAnEemBZcpC9gfPbA : String, chatbotMatchingWords_Ln590cAnEemBZcpC9gfPbA : String,
         chatbotText_psWB0cAnEemBZcpC9gfPbA : String, chatbotMatchingWords_psWB0cAnEemBZcpC9gfPbA : String,
         chatbotText_rTQVQcAnEemBZcpC9gfPbA : String, chatbotMatchingWords_rTQVQcAnEemBZcpC9gfPbA : String): String =
    s"""
       |	$chatbotText_wKSLEMAmEemBZcpC9gfPbA
       |   $chatbotMatchingWords_wKSLEMAmEemBZcpC9gfPbA
       |	$chatbotText_xL4K8cAmEemBZcpC9gfPbA
       |   $chatbotMatchingWords_xL4K8cAmEemBZcpC9gfPbA
       |	$chatbotText__nanccAmEemBZcpC9gfPbA
       |   $chatbotMatchingWords__nanccAmEemBZcpC9gfPbA
       |	$chatbotText_JBWyUcAnEemBZcpC9gfPbA
       |   $chatbotMatchingWords_JBWyUcAnEemBZcpC9gfPbA
       |	$chatbotText_JoBJscAnEemBZcpC9gfPbA
       |   $chatbotMatchingWords_JoBJscAnEemBZcpC9gfPbA
       |	$chatbotText_Kr62scAnEemBZcpC9gfPbA
       |   $chatbotMatchingWords_Kr62scAnEemBZcpC9gfPbA
       |	$chatbotText_Ln590cAnEemBZcpC9gfPbA
       |   $chatbotMatchingWords_Ln590cAnEemBZcpC9gfPbA
       |	$chatbotText_psWB0cAnEemBZcpC9gfPbA
       |   $chatbotMatchingWords_psWB0cAnEemBZcpC9gfPbA
       |	$chatbotText_rTQVQcAnEemBZcpC9gfPbA
       |   $chatbotMatchingWords_rTQVQcAnEemBZcpC9gfPbA
    """.stripMargin
    val semanticType: Type =  'ChatbotText_wKSLEMAmEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_wKSLEMAmEemBZcpC9gfPbA =>: 'ChatbotText_xL4K8cAmEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_xL4K8cAmEemBZcpC9gfPbA =>: 'ChatbotText__nanccAmEemBZcpC9gfPbA =>: 'ChatbotMatchingWords__nanccAmEemBZcpC9gfPbA =>: 'ChatbotText_JBWyUcAnEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_JBWyUcAnEemBZcpC9gfPbA =>: 'ChatbotText_JoBJscAnEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_JoBJscAnEemBZcpC9gfPbA =>: 'ChatbotText_Kr62scAnEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_Kr62scAnEemBZcpC9gfPbA =>: 'ChatbotText_Ln590cAnEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_Ln590cAnEemBZcpC9gfPbA =>: 'ChatbotText_psWB0cAnEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_psWB0cAnEemBZcpC9gfPbA =>: 'ChatbotText_rTQVQcAnEemBZcpC9gfPbA =>: 'ChatbotMatchingWords_rTQVQcAnEemBZcpC9gfPbA =>: 'ProblemdetectionAndSolution
  }

  @combinator object ChatbotText_wKSLEMAmEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_wKSLEMAmEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Wie lautet die Modellnummer Ihres Kühlschrank? Die Modellnummer steht auf der Innenseite der Tür."+System.lineSeparator();
       |						System.out.println("Wie lautet die Modellnummer Ihres Kühlschrank? Die Modellnummer steht auf der Innenseite der Tür.");
       |						findMatchingRule("M_wKSLEMAmEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_wKSLEMAmEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_wKSLEMAmEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_wKSLEMAmEemBZcpC9gfPbA":
            |											if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |											tRule.put("KS1","T_JBWyUcAnEemBZcpC9gfPbA");
       |											tRule.put("KS2","T_JBWyUcAnEemBZcpC9gfPbA");
       |											tRule.put("KS3","T_JoBJscAnEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_wKSLEMAmEemBZcpC9gfPbA
  }
  @combinator object ChatbotText_xL4K8cAmEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_xL4K8cAmEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Wie lautet die Modellnummer Ihrer Mikrowelle? Die Modellnummer steht auf der Innenseite der Tür."+System.lineSeparator();
       |						System.out.println("Wie lautet die Modellnummer Ihrer Mikrowelle? Die Modellnummer steht auf der Innenseite der Tür.");
       |						findMatchingRule("M_xL4K8cAmEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_xL4K8cAmEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_xL4K8cAmEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_xL4K8cAmEemBZcpC9gfPbA":
            |											if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |											tRule.put("MW1","T_Kr62scAnEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_xL4K8cAmEemBZcpC9gfPbA
  }
  @combinator object ChatbotText__nanccAmEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T__nanccAmEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Wie lautet die Modellnummer Ihrer Ofen/Herd-Kombination? Die Modellnummer steht auf der Innenseite der Tür."+System.lineSeparator();
       |						System.out.println("Wie lautet die Modellnummer Ihrer Ofen/Herd-Kombination? Die Modellnummer steht auf der Innenseite der Tür.");
       |						findMatchingRule("M__nanccAmEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText__nanccAmEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords__nanccAmEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M__nanccAmEemBZcpC9gfPbA":
            |											if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |											tRule.put("HO1","T_Ln590cAnEemBZcpC9gfPbA");
       |											tRule.put("HO2","T_Ln590cAnEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords__nanccAmEemBZcpC9gfPbA
  }
  @combinator object ChatbotText_JBWyUcAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_JBWyUcAnEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Welche Komponente an Ihrem Kühlschrank ist defekt?"+System.lineSeparator();
       |						System.out.println("Welche Komponente an Ihrem Kühlschrank ist defekt?");
       |						findMatchingRule("M_JBWyUcAnEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_JBWyUcAnEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_JBWyUcAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_JBWyUcAnEemBZcpC9gfPbA":
            |											if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |											tRule.put("Lampe","T_rTQVQcAnEemBZcpC9gfPbA");
       |											tRule.put("Licht","T_rTQVQcAnEemBZcpC9gfPbA");
       |											tRule.put("Stromversorgung","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Kühlung","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Temperatur","T_psWB0cAnEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_JBWyUcAnEemBZcpC9gfPbA
  }
  @combinator object ChatbotText_JoBJscAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_JoBJscAnEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Welche Komponente an Ihrem Kühlschrank mit Eismaschine ist defekt?"+System.lineSeparator();
       |						System.out.println("Welche Komponente an Ihrem Kühlschrank mit Eismaschine ist defekt?");
       |						findMatchingRule("M_JoBJscAnEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_JoBJscAnEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_JoBJscAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_JoBJscAnEemBZcpC9gfPbA":
            |											if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |											tRule.put("Stromversorgung","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Kühlung","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Temperatur","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Eismaschine","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Eis","T_psWB0cAnEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_JoBJscAnEemBZcpC9gfPbA
  }
  @combinator object ChatbotText_Kr62scAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_Kr62scAnEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Welche Komponente an Ihrer Mikrowelle ist defekt?"+System.lineSeparator();
       |						System.out.println("Welche Komponente an Ihrer Mikrowelle ist defekt?");
       |						findMatchingRule("M_Kr62scAnEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_Kr62scAnEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_Kr62scAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_Kr62scAnEemBZcpC9gfPbA":
            |											if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |											tRule.put("Teller","T_rTQVQcAnEemBZcpC9gfPbA");
       |											tRule.put("Stromversorgung","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Lampe","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Anzeigefeld","T_psWB0cAnEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_Kr62scAnEemBZcpC9gfPbA
  }
  @combinator object ChatbotText_Ln590cAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_Ln590cAnEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Welche Komponente an Ihrer Ofen/Herdkombination ist defekt?"+System.lineSeparator();
       |						System.out.println("Welche Komponente an Ihrer Ofen/Herdkombination ist defekt?");
       |						findMatchingRule("M_Ln590cAnEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_Ln590cAnEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_Ln590cAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_Ln590cAnEemBZcpC9gfPbA":
            |											if(sc.hasNext()) nextLine = sc.nextLine();
       |											savedUserData+="User: "+nextLine+System.lineSeparator();
       |											tRule = new HashMap<>();
       |											tRule.put("Stromversorgung","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Ofen","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Herdplatte","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Eingabefeld","T_psWB0cAnEemBZcpC9gfPbA");
       |											tRule.put("Anzeige","T_psWB0cAnEemBZcpC9gfPbA");
       |											findMatchingRule(findMatchingWord(tRule, nextLine));
       |											break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_Ln590cAnEemBZcpC9gfPbA
  }
  @combinator object ChatbotText_psWB0cAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_psWB0cAnEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Es handelt sich um ein schwerwiegendes Problem, wir senden Ihnen ein neues Gerät zu."+System.lineSeparator();
       |						System.out.println("Es handelt sich um ein schwerwiegendes Problem, wir senden Ihnen ein neues Gerät zu.");
       |						findMatchingRule("M_psWB0cAnEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_psWB0cAnEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_psWB0cAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_psWB0cAnEemBZcpC9gfPbA":
             |	findMatchingRule("Adresse");
           |					break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_psWB0cAnEemBZcpC9gfPbA
  }
  @combinator object ChatbotText_rTQVQcAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "T_rTQVQcAnEemBZcpC9gfPbA":
       |						savedUserData+="Chatbot:Es handelt sich um ein kleineres Problem, wir senden Ihnen das passende Ersatzteil inkl. Anleitung zu."+System.lineSeparator();
       |						System.out.println("Es handelt sich um ein kleineres Problem, wir senden Ihnen das passende Ersatzteil inkl. Anleitung zu.");
       |						findMatchingRule("M_rTQVQcAnEemBZcpC9gfPbA");
       |						break;
     """.stripMargin
    val semanticType: Type = 'ChatbotText_rTQVQcAnEemBZcpC9gfPbA
  }

  @combinator object ChatbotMatchingWords_rTQVQcAnEemBZcpC9gfPbA {
    def apply: String =
    s"""
       |			case "M_rTQVQcAnEemBZcpC9gfPbA":
             |	findMatchingRule("Adresse");
           |					break;
     """.stripMargin
    val semanticType: Type = 'ChatbotMatchingWords_rTQVQcAnEemBZcpC9gfPbA
  }

  @combinator object Addressrecognition {
    def apply: String =
    s"""
       |			case "Address":
       |			case "Adresse":
       |			case "addressrecognition":
       |							if(knowingAddress){
       |								findMatchingRule("Conclusion");
       |								break;
       |							} else{
       |								String name = "", land = "", street = "", zip = "", city = "", correctInput = "";
       |								System.out.println("Nun wird Ihre Adresse aufgenommen. Bitte antworten Sie nur mit dem korrekten Eingabewort.");
       |								System.out.println("Wie lautet Ihr vollständiger Name?");
       |								if(sc.hasNext()) name = sc.nextLine();
       |								if(name.equals(":back")||name.equals(":b")||name.equals(":zurück")||name.equals(":z")){
       |									savedUserData+="User: :back"+System.lineSeparator();
       |									lastRules.pop();
       |									findMatchingRule(lastRules.peek());
       |									break;
       |								}
       |								System.out.println("In welchem Land befindet sich Ihre Adresse?");
       |								if(sc.hasNext()) land = sc.nextLine();
       |								if(land.equals(":back")||land.equals(":b")||land.equals(":zurück")||land.equals(":z")){
       |									savedUserData+="User: :back"+System.lineSeparator();
       |									lastRules.pop();
       |									findMatchingRule(lastRules.peek());
       |									break;
       |								}
       |								System.out.println("Wie lautet Ihre Straße inkl. Hausnummer?");
       |								if(sc.hasNext()) street = sc.nextLine();
       |								if(street.equals(":back")||street.equals(":b")||street.equals(":zurück")||street.equals(":z")){
       |									savedUserData+="User: :back"+System.lineSeparator();
       |									lastRules.pop();
       |									findMatchingRule(lastRules.peek());
       |									break;
       |								}
       |								System.out.println("Wie lautet Ihre Postleitzahl?");
       |								if(sc.hasNext()) zip = sc.nextLine();
       |								if(zip.equals(":back")||zip.equals(":b")||zip.equals(":zurück")||zip.equals(":z")){
       |									savedUserData+="User: :back"+System.lineSeparator();
       |									lastRules.pop();
       |									findMatchingRule(lastRules.peek());
       |									break;
       |								}
       |								System.out.println("Wie lautet Ihre Stadt?");
       |								if(sc.hasNext()) city = sc.nextLine();
       |								if(city.equals(":back")||city.equals(":b")||city.equals(":zurück")||city.equals(":z")){
       |									savedUserData+="User: :back"+System.lineSeparator();
       |									lastRules.pop();
       |									findMatchingRule(lastRules.peek());
       |									break;
       |								}
       |								System.out.println("Vielen Dank, Ihre Adresse wurde aufgenommen und lautet wie folgt:");
       |								if(!name.equals("")) System.out.println("Name: "+name);
       |								if(!land.equals("")) System.out.println("Land: "+land);
       |								if(!street.equals("")) System.out.println("Straße: "+street);
       |								if(!zip.equals("")) System.out.println("Postleitzahl: "+zip);
       |								if(!city.equals("")) System.out.println("Stadt: "+city);
       |								savedUserData+="User Adresse:"+System.lineSeparator()+"Name: "+name+System.lineSeparator()+"Land: "+land+System.lineSeparator()+"Straße: "+street+System.lineSeparator()+"Postleitzahl: "+zip+System.lineSeparator()+" Stadt: "+city+System.lineSeparator();
       |								savedUserData+="Chatbot: Sind Ihre Eingaben korrekt? (Ja/Nein)"+System.lineSeparator();
       |								System.out.println("Sind Ihre Eingaben korrekt? (Ja/Nein)");
       |								if(sc.hasNext()) correctInput = sc.nextLine();
       |								savedUserData+="User: "+correctInput +System.lineSeparator();
       |								if(correctInput.equals("Ja") || correctInput.equals("ja")||correctInput.equals("J")||correctInput.equals("j")||correctInput.equals("Yes")||correctInput.equals("yes")||correctInput.equals("y")||correctInput.equals("Y")){
       |									knowingAddress = true;
       |									findMatchingRule("Conclusion");
       |								} else{
       |									savedUserData+="Chatbot: Da Ihre Eingaben nicht korrekt waren, müssen Sie diese nun erneut eingeben."+System.lineSeparator();
       |									System.out.println("Da Ihre Eingaben nicht korrekt waren, müssen Sie diese nun erneut eingeben.");
       |									findMatchingRule("Adresse");
       |								}
       |								break;
       |							}
       |
     """.stripMargin
    val semanticType: Type = 'Addressrecognition
  }

  @combinator object Conclusion {
    def apply (loop: String, saveUserData: String): String =
    s"""
       |			case "Conclusion":
       |					String loop="";
       |					savedUserData+="Chatbot: Vielen Dank. Wir kümmern uns nun um Ihr Anliegen."+System.lineSeparator();
       |					savedUserData+="Chatbot: Bei weiteren Fragen melden Sie sich bitte unter der angegebenen Hotline."+System.lineSeparator();
       |					System.out.println("Vielen Dank. Wir kümmern uns nun um Ihr Anliegen.");
       |					System.out.println("Bei weiteren Fragen melden Sie sich bitte unter der angegebenen Hotline.");
       |     $loop
       |		 $saveUserData
       |					break;
       |
     """.stripMargin
    val semanticType: Type = 'Loop(loopFlag) =>: 'SaveUserData(saveUserDataFlag) =>: 'Conclusion(loopFlag, saveUserDataFlag)
  }

  @combinator object TrueSaveUserData {
    def apply: String =
    s"""
       |		try{
       |						FileWriter fw = new FileWriter("Chatbot-"+sdf.format(date)+".txt");
       |						BufferedWriter bw = new BufferedWriter(fw);
       |						bw.write(savedUserData);
       |						bw.close();
       |					} catch(IOException e){
       |						System.out.println("Daten konnten nicht gespeichert werden.");
       |					}
     """.stripMargin
    val semanticType: Type = 'SaveUserData('true)
  }

  @combinator object FalseSaveUserData {
    def apply: String =""
    val semanticType: Type = 'SaveUserData('false)
  }

  @combinator object TrueLoop {
    def apply: String =
    s"""
       |					savedUserData+="Chatbot: Ist noch ein weiteres Produkt defekt? (Ja/Nein)"+System.lineSeparator();
       |					System.out.println("Ist noch ein weiteres Produkt defekt? (Ja/Nein)");
       |					if(sc.hasNext()) loop = sc.nextLine();
       |					savedUserData+="User: "+loop+System.lineSeparator();
       |					if(loop.equals("Ja") || loop.equals("ja")|| loop.equals("yes")|| loop.equals("y")|| loop.equals("Y")|| loop.equals("J")|| loop.equals("j")){
       |						lastRules.push("StartID");
       |						findMatchingRule("StartID");
       |					}else{
       |					savedUserData+="Chatbot: Dankeschön für Ihre Geduld. Wir hoffen dass Sie Ihr Produkt bald wieder nutzen können."+System.lineSeparator();
       |						System.out.println("Dankeschön für Ihre Geduld. Wir hoffen dass Sie Ihr Produkt bald wieder nutzen können.");
       |					}
     """.stripMargin
    val semanticType: Type = 'Loop('true)
  }

  @combinator object FalseLoop {
    def apply: String =""
    val semanticType: Type = 'Loop('false)
  }

  @combinator object HelpMethods {
    def apply: String =
    s"""
       |	private static String findMatchingWord(HashMap<String, String> tRule, String nextLine) {
       |		String bestMatchKey = "";
       |		ArrayList<String> bestMatch = new ArrayList<>();
       |		int minLev = 3;
       |		String[] words = nextLine.split("\\\\s+");
       |		for(String word : words){
       |			if(word.equalsIgnoreCase(":back")||word.equalsIgnoreCase(":b")||nextLine.equalsIgnoreCase(":zurück")||nextLine.equalsIgnoreCase(":z")){
       |				savedUserData+="User: :back"+System.lineSeparator();
       |				if(lastRules.peek().equals("StartID") && lastRules.size()==1){
       |					return "StartID";
       |				}
       |				lastRules.pop();
       |				return lastRules.peek();
       |			}
       |		}
       |		Set<String> matchingWords = tRule.keySet();
       |		for(String matchingWord : matchingWords){
       |			for(String inputWord : words){
       |				int current = StringUtils.getLevenshteinDistance(inputWord, matchingWord);
       |				if (current < minLev){
       |					minLev = current;
       |					bestMatch.clear();
       |					bestMatch.add(matchingWord);
       |				}
       |				if(current == minLev){
       |					bestMatch.add(matchingWord);
       |				}
       |			}
       |		}
       |		HashSet<String> set = new HashSet<String>();
       |		set.addAll(bestMatch);
       |		bestMatch.clear();
       |		bestMatch.addAll(set);
       |
       |		if(bestMatch.size()>0){
       |			if(bestMatch.size()==1){
       |				//System.out.println(minLev);
       |         				bestMatchKey = tRule.get(bestMatch.get(0));
       |         				lastRules.push(bestMatchKey);
       |         				return bestMatchKey;
       |         			} else{
       |            			bestMatchKey = tRule.get(selectMatchingWord(bestMatch));
       |         				lastRules.push(bestMatchKey);
       |         				return bestMatchKey;
       |         			}
       |         		}
       |         		else {
       |         			ArrayList<String> noMatch = new ArrayList<>();
       |         			noMatch.addAll(tRule.keySet());
       |         			bestMatchKey = tRule.get(selectMatchingWord(noMatch));
       |         			lastRules.push(bestMatchKey);
       |         			return bestMatchKey;
       |         		}
       |         	}
       |
       |         	private static String selectMatchingWord(ArrayList<String> match) {
       |         	savedUserData+="Chatbot: Ihre Eingabe konnte nicht eindeutig zugeordnet werden."+System.lineSeparator();
       |         	savedUserData+="Chatbot: Bitte tippen Sie die Zahl des passenden Begriffs ein."+System.lineSeparator();
       |         	savedUserData+="Chatbot: Sofern keiner der Begriffe Ihr Problem beschreibt, melden Sie sich bei der angegebenen Hotline."+System.lineSeparator();
       |         		System.out.println("Ihre Eingabe konnte nicht eindeutig zugeordnet werden.");
       |         		System.out.println("Bitte tippen Sie die Zahl des passenden Begriffs ein.");
       |         		System.out.println("Sofern keiner der Begriffe Ihr Problem beschreibt, melden Sie sich bei der angegebenen Hotline.");
       |         		String out = "";
       |         		int counter = 0;
       |         		for(int i=0; i<match.size(); i++){
       |         			out += "[ "+i+ ": "+ match.get(i)+" ]  ";
       |         			counter ++;
       |         			if(counter >4){
       |         				counter = 0;
       |         				savedUserData+="Chatbot: "+out+System.lineSeparator();
       |				System.out.println(out);
       |				out = "";
       |			}
       |		}
       |		if(!out.equals("")){
       |			savedUserData+="Chatbot: "+out+System.lineSeparator();
       |			System.out.println(out);
       |		}
       |		if(sc.hasNextLine()){
       |			try{
       |				int selection = Integer.parseInt(sc.nextLine());
       |				savedUserData+="User: "+selection+System.lineSeparator();
       |				if(selection >=0 && selection < match.size()){
       |					return match.get(selection);
       |				}else{
       |
       |					return selectMatchingWord(match);
       |				}
       |			}
       |			catch(NumberFormatException e){
       |				return selectMatchingWord(match);
       |			}
       |		}
       |			return "";
       |	}
     """.stripMargin
    val semanticType: Type = 'HelpMethods
  }
  */
  //endregion
}

