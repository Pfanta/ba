package org.combinators.cls.scheduling.scala

import org.combinators.cls.interpreter._
import org.combinators.cls.scheduling.model.ShopClass
import org.combinators.cls.scheduling.utils.ClassificationUtils.Classification
import org.combinators.cls.types.Constructor
import org.combinators.cls.types.syntax._

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object Scheduler {
  lazy val reflectedRepository: ReflectedRepository[Repository] = ReflectedRepository(repository, substitutionSpace = repository.shopClassKinding, classLoader = this.getClass.getClassLoader)

  lazy val repository: Repository = new Repository {}

  def run(classification: Classification): java.util.List[String] = {

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
    val results = new ListBuffer[String]()
    var b = true
    var i = 0
    while (b) {
      try {
        results += inhabitationResult.interpretedTerms.index(i)
        i += 1
      } catch {
        case _: java.lang.IndexOutOfBoundsException => b = false
      }
    }
    results.toList.asJava
  }

  trait Repository extends RunnerRepository with AlgorithmRepository with HeuristicRepository {}

}