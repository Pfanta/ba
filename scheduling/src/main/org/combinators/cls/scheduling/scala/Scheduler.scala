package org.combinators.cls.scheduling.scala

import org.combinators.cls.inhabitation.Tree
import org.combinators.cls.interpreter._
import org.combinators.cls.scheduling.model.{Classification, ShopClass}
import org.combinators.cls.types.Constructor
import org.combinators.cls.types.syntax._

import scala.collection.JavaConverters._

object Scheduler {
  lazy val repository: Repository = new Repository {}
  lazy val reflectedRepository: ReflectedRepository[Repository] = ReflectedRepository(repository, substitutionSpace = repository.shopClassKinding, classLoader = this.getClass.getClassLoader)
  var tree: Seq[Tree] = _

  def run(classification: Classification): java.util.Map[String, String] = {

    var targetType: Constructor = 'Scheduler('NONE)
    classification.getShopClass match {
      case ShopClass.FS => targetType = 'Scheduler('FS)
      case ShopClass.FFS => targetType = 'Scheduler('FFS)
      case ShopClass.JS => targetType = 'Scheduler('JS)
      case ShopClass.FJS => targetType = 'Scheduler('FJS)
      case ShopClass.OS => targetType = 'Scheduler('OS)
      case _ => targetType = 'Scheduler('NONE)
    }

    val inhabitationResult: InhabitationResult[String] = reflectedRepository.inhabit[String](targetType)
    val results = new java.util.TreeMap[String, String]()
    var b = true
    var i = 0
    while (b) {
      try {

        var heuristic = ""
        val l1 = seqAsJavaList(inhabitationResult.terms.index(i).arguments).get(0)
        if (l1.name.equals("NEH"))
          heuristic = l1.name
        else if (l1.name.equals("RandomPermutationFlowShop"))
          heuristic = "Iterated Random"
        else
          heuristic = seqAsJavaList(l1.arguments).get(0).name

        results.put(heuristic, inhabitationResult.interpretedTerms.index(i))

        i += 1
      } catch {
        case _: java.lang.IndexOutOfBoundsException => b = false
      }
    }
    results
  }

  trait Repository extends RunnerRepository with AlgorithmRepository with HeuristicRepository {}

}