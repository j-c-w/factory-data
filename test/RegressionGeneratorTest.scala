import backend.scala.graphing.regressions.{NoRegression, RegressionGenerator}
import org.specs2.mutable.Specification

/**
 * Created by Jackson on 2/6/2015.
 *
 * This is a test designed in the same style as the
 * DataField test. It ensures that all the values
 * defined as possible selection as strings in the
 * regression generator return reasonable values when
 * parsed
 */
class RegressionGeneratorTest extends Specification {
  "All values " should {
    " not return no regression " in {
      val regressionTypes = RegressionGenerator.toList
      val returnedValues = regressionTypes map {
        x => RegressionGenerator.fromString(x, "TEST_MODE")
      }
      returnedValues foreach {
        _ mustNotEqual NoRegression
      }
    }
  }
}
