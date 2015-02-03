import backend.scala.datatypes.DataField
import org.specs2.mutable.Specification

/**
 * Created by Jackson on 2/3/2015.
 *
 * This tests ensures that if a field in one of the lists provided
 * in the DataField its string can also be converted back into some
 * not NoData field (with the same .toString as the input string)
 */
class DataFieldTest extends Specification {
  "The .toString of all the output objects " should {
    " match the input string " in {
      val inputs = DataField.asList
      val outputs = inputs map {
        input => DataField.fromString(input).toString
      }

      (outputs zip inputs) map {
        case (output, input) => output.mustEqual(input)
      }
    }

    " match the input string " in {
      val inputs = DataField.asDoublesList

      val outputs = inputs map {
        input => DataField.fromString(input).toString
      }

      (outputs zip inputs) map {
        case (output, input) => output.mustEqual(input)
      }
    }
  }
}
