package scala

import org.combinators.cls.interpreter._
import org.combinators.cls.types.syntax._
import utils.ClassificationUtils.Classification

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object Scheduler {

  lazy val repository: Repository = new Repository {}
  lazy val reflectedRepository: ReflectedRepository[Repository] = ReflectedRepository(repository, substitutionSpace = repository.kinding, classLoader = this.getClass.getClassLoader)

  def run(classification: Classification): java.util.List[String] = {

    //TODO: ShopClass from classification

    lazy val inhabitationResult: InhabitationResult[String] = reflectedRepository.inhabit[String]('Scheduler('false))

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

  trait Repository extends AlgorithmRepository {
    //TODO: Add Runner Body
  }

}
