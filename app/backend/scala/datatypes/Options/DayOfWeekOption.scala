package backend.scala.datatypes.options

/**
 * Created by Jackson on 4/7/2015.
 *
 * This is an option class that is just an extension of the
 * StringOption class. True extension cannot happen becase
 * of the (in retrospect, too strict) types on things
 * like mergeEqual.
 *
 * The only difference is that there are only 7 different values
 * it can take (Monday - Sunday)
 */
abstract class DayOfWeekOption extends MathComparable[DayOfWeekOption] {
  override def compareTo(other: DayOfWeekOption) = (this, other) match {
    case (SomeDayOfWeek(_, order), SomeDayOfWeek(_, orderTwo)) => order compareTo orderTwo
    case (NoDayOfWeek, SomeDayOfWeek(_, _)) => -1
    case (SomeDayOfWeek(_, _), NoDayOfWeek) => 1
    case (NoDayOfWeek, NoDayOfWeek) => 0
  }

  def mergeEqual(other: DayOfWeekOption) =
    if ((this compareTo other) == 0) this
    else NoDayOfWeek

  def isNone = isEmpty

  def isEmpty: Boolean
  def get: String
  def getOrElse(x: => String): String
}

object NoDayOfWeek extends DayOfWeekOption {
  override def isEmpty: Boolean = true

  override def get: String = throw new NoSuchElementException("NoDayOfWeek.get")

  override def getOrElse(x: => String): String = x
}

abstract case class SomeDayOfWeek(day: String, order: Int) extends DayOfWeekOption {
  override def isEmpty: Boolean = false

  override def get: String = day

  override def getOrElse(x: => String): String = get

  override val toString = day
}

object Sunday extends SomeDayOfWeek("Sunday", 0)
object Monday extends SomeDayOfWeek("Monday", 1)
object Tuesday extends SomeDayOfWeek("Tuesday", 2)
object Wednesday extends SomeDayOfWeek("Wednesday", 3)
object Thursday extends SomeDayOfWeek("Thursday", 4)
object Friday extends SomeDayOfWeek("Friday", 5)
object Saturday extends SomeDayOfWeek("Saturday", 6)

object DayOfWeekOption {
  implicit def fromOrderString(dayOfWeek: String): DayOfWeekOption = dayOfWeek match {
    case "0" => Sunday
    case "1" => Monday
    case "2" => Tuesday
    case "3" => Wednesday
    case "4" => Thursday
    case "5" => Friday
    case "6" => Saturday
    case _ => NoDayOfWeek
  }

  implicit def fromDayString(dayOfWeek: String): DayOfWeekOption = dayOfWeek match {
    case Sunday.toString => Sunday
    case Monday.toString => Monday
    case Tuesday.toString => Tuesday
    case Wednesday.toString => Wednesday
    case Thursday.toString => Thursday
    case Friday.toString => Friday
    case Saturday.toString => Saturday
    case _ => NoDayOfWeek
  }
}