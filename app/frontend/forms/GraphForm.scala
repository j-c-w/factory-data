package frontend.forms

import play.api.data.Form
import play.api.data.Forms._


/*
 * Created by Jackson Woodruff on 19/10/2014 
 *
 * This is similar to the DataManipulation form
 * in that it is really a pointless holder object
 */

object GraphForm {
  val form = Form(
    mapping (
      "xAxis" -> text,
      "yAxis" -> text,
      "Graph Type" -> text
    ) (GraphFormParser.apply) (GraphFormParser.unapply)
  )
}
