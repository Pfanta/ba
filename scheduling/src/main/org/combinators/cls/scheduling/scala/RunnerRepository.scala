package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.syntax._
import org.combinators.cls.types.{Kinding, Type, Variable}

trait RunnerRepository {

  lazy val shopClass: Variable = Variable("shopClass")
  lazy val shopClassKinding: Kinding = Kinding(shopClass)
    .addOption(problemClass.flowShop)
    .addOption(problemClass.flexibleFlowShop)
    .addOption(problemClass.jobShop)
    .addOption(problemClass.flexibleJobShop)
    .addOption(problemClass.openShop)
    .addOption(problemClass.none)

  object problemClass {
    val flowShop: Type = 'FS
    val flexibleFlowShop: Type = 'FFS
    val jobShop: Type = 'JS
    val flexibleJobShop: Type = 'FJS
    val openShop: Type = 'OS
    val none: Type = 'NONE

    def apply(part: Type): Type = 'ProblemClass(part)
  }

  @combinator object Scheduler {
    val semanticType: Type = 'Algorithm :&: shopClass =>: 'Scheduler(shopClass)

    def apply(Algorithm: String): String =
      s"""|package org.combinators.cls.scheduling;
          |
          |import java.util.*;
          |import java.util.function.Function;
          |import org.combinators.cls.scheduling.model.*;
          |import org.combinators.cls.scheduling.utils.*;
			    |
			    |public class Runner implements java.util.function.Function<Classification, Task> {
			    | public Task apply(Classification classification) {
          |        $Algorithm
          |        schedule.setResult(schedule.getMakespan());
          |        return schedule;
          |    }
          |}
			    """.stripMargin
  }
}
