package controllers

import play.api._
import play.api.mvc._
import play.api.templates.Html
import play.api.data.Form
import play.api.data.Forms._
import models.User

object Application extends Controller {
  var users: List[User] = Nil

  val userform = Form(
    mapping(
      "username" -> nonEmptyText)(User.apply)(User.unapply))

  def index = Action {
        Ok(views.html.index(userform, users))
//    users match {
//      case _ :: tail => Ok(views.html.index(userform, users))
//      case Nil => Ok(views.html.index(userform))
//    }
  }

  def submitUsername = Action { implicit req =>
    val form = userform.bindFromRequest()
    if (users == Nil) {
      println("creating list as it was null")
      users = List(form.get)
    }
    else {
      users = form.get :: users
      //users ++= List(form.get)
  	}
    println("Added user: "+form.get.username+" to list "+users)
    Redirect(routes.Application.index())
  }

}
