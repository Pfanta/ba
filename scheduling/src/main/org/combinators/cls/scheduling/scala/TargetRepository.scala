package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter.combinator
import org.combinators.cls.types.Type
import org.combinators.cls.types.syntax._

trait TargetRepository {

  @combinator object TargetMakespan {
    val semanticType: Type = 'Target

    def apply(Algorithm: String, Target: String): String = "schedule.setResult(schedule.getMakespan());"
  }

  @combinator object TargetTWT {
    val semanticType: Type = 'Target2

    def apply(Algorithm: String, Target: String): String = "throw new java.lang.UnsupportedOperationException(\"Not yet implemented.\")"
  }

}