package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.syntax._
import org.combinators.cls.types.{Kinding, Type, Variable}

trait RunnerRepository {
  lazy val shopClass: Variable = Variable("algorithm")

  lazy val shopClassKinding: Kinding = Kinding(shopClass)
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
          |import java.util.*;
          |import java.util.function.Function;
          |import org.combinators.cls.scheduling.model.*;
          |import org.combinators.cls.scheduling.utils.*;
			    |
			    |public class Runner implements java.util.function.Function<ClassificationUtils.Classification, Task> {
			    | public Task apply(ClassificationUtils.Classification classification) {
          |        $Algorithm
          |        schedule.setResult(schedule.getMakespan());
          |        return schedule;
          |    }
          |}
			    """.stripMargin
  }
}
