package main.scala.database

import java.sql.ResultSet

import main.scala.datatypes.{NoDouble, NoInteger, EmployeeTypes, LineListObject}
import org.skife.jdbi.v2.tweak.ResultSetMapper
import org.skife.jdbi.v2.{StatementContext, Handle, DBI}

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

object DatabaseHandler {
  val dbi: DBI = new DBI("C:\\Users\\Jackson\\Projects\\IPA\\Libraries\\data\\1004 attendance.sqlite")
  val handle: Handle = dbi.open()
  handle.execute()
}

object LineListObjectMapper extends ResultSetMapper[LineListObject] {
  def map(index: Int, resultSet: ResultSet, context: StatementContext): LineListObject =
    new LineListObject(
      new EmployeeTypes(NoInteger, NoInteger, NoInteger, NoDouble, NoInteger),
      new EmployeeTypes()
    )
}
