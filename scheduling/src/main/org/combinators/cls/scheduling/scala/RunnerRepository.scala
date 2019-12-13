package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.syntax._
import org.combinators.cls.types.{Kinding, Type, Variable}

trait RunnerRepository {
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
      s"""|package org.combinators.cls.scheduling;
          |
          |import org.combinators.cls.scheduling.model.Task;
			    |
			    |public class Runner implements java.util.function.Function<Task, Task> {
			    | public Task apply(Task task) {
          |   task.setResult(new java.util.Random().nextInt(50));
			    |   return task;
			    | }
          |}
			    """.stripMargin
  }

}
