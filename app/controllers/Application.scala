package controllers

import play.api._
import play.api.mvc._
import play.api.templates.Html
import play.api.data.Form
import play.api.data.Forms._
import models.User
import play.api.libs.ws.WS
import scala.concurrent.ExecutionContext._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsValue

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
      users = List(form.get)
    }
    else {
      users = form.get :: users
      //users ++= List(form.get)
  	}
    Redirect(routes.Application.index())
  }
  
  def lookupCompany = Action { 
    Async {
	    WS.url("http://maps.googleapis.com/maps/api/geocode/json?address=Goodge+Street&sensor=false").get()
	    	.map {response =>  Ok((response.json \ "results" \ "address_components").as[JsValue]) }
    }
  
  }

}
