import backend.scala.datatypes.options.{NoInteger, SomeDouble, SomeInteger}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.specs2.mutable.Specification

/*
 * Created by Jackson Woodruff on 22/08/2014 
 *
 */


@RunWith(classOf[JUnitRunner])
class OptionTest extends Specification {
  "Integer Comparison" should {
    "All be true" in {
      val three = SomeInteger(3)
      val four = SomeInteger(4)
      val five = SomeInteger(5)
      val otherThree = SomeInteger(3)
      val noInteger1 = NoInteger
      val noInteger2 = NoInteger

      (three == otherThree) must equalTo(true)
      (three < four) must equalTo(true)
      (five > three) must equalTo(true)
      (noInteger1 == noInteger2) must equalTo(true)
      (three <= four) must equalTo(true)
      (three <= otherThree) must equalTo(true)
      (five >= three) must equalTo(true)
      (three >= otherThree) must equalTo(true)
    }
  }

  "Double Comparison" should {
    "All be false" in {
      val half = SomeDouble(0.5)
      val quarter = SomeDouble(0.25)
      val oneAndHalf = SomeDouble(1.5)

      half == quarter must equalTo (false)
      half < quarter must equalTo (false)
      half > oneAndHalf must equalTo (false)
    }

    "All be true in" in {
      val half = SomeDouble(0.5)
      val halfAgain = SomeDouble(0.5)

      half == halfAgain must equalTo (true)
      half <= halfAgain must equalTo (true)
    }
  }
}
