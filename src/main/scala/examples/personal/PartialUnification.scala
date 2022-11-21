package examples.personal

import cats.Functor
import cats.syntax.functor._
import cats.instances.function._

object PartialUnification {
  //----------------------------
  // example 1
  val func1 = (x: Int) => x.toDouble
  val func2 = (x: Double) => x * 2
  val func3 = (x: Double) => x.toString

  // similar effect to andThen composition
  func1.map(func2)

  func1.map(func3)


  //-----------------------------
  // example 2
  def foo[F[_], A](fa: F[A]): String = fa.toString
  val sth = foo { x: Int => x * 2 }
  val sth2 = foo { x: Int => x.toDouble }
}
