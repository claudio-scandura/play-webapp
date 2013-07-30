package controllers

import play.api._
import play.api.mvc._
import play.api.templates.Html
import play.api.data.Form
import play.api.data.Forms._
import models.UserComment
import play.api.libs.ws.WS
import scala.concurrent.ExecutionContext._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsValue
import akka.actor.Props
import actors.BasicActor
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import models.UserComment
import actors.Username
import akka.util.Timeout
import play.api.libs.concurrent.Akka
import play.api.Play.current
import models.UserCommentDAO
import models.SaveComment
import models.GetAllComments
import models.UserComment
import models.UserComment
import org.springframework.scheduling.annotation.Async
import models.CommentList
import play.api.libs.json.Json
import models.UserComment
import views.html.defaultpages.badRequest
import models.UserComment
import models.UserComment
import models.UserComment
import play.api.libs.json.JsArray
import play.api.libs.json.JsArray
import models.CommentList
import play.api.libs.json.JsObject
import play.api.libs.json.JsResult

object Application extends Controller {

  private val userCommentService = Akka.system.actorOf(Props[UserCommentDAO])
  private implicit val timeout = new Timeout(5 seconds)

  val userform = Form(
    mapping(
      "username" -> nonEmptyText,
      "comment" -> nonEmptyText)(UserComment.apply)(UserComment.unapply))

  def index = Action {
    AsyncResult(
      (userCommentService ? GetAllComments(false))
        .mapTo[CommentList]
        .map(result => Ok(views.html.index(userform, result.comments))))
  }

  def submitCommentAjax = Action { request =>
    request.body.asJson match {
      case Some(value) => {
        val comment = UserComment.userCommentReads.reads(value).get
        AsyncResult(
          (userCommentService ?
            SaveComment(comment))
            .map(result => Ok(Json.toJson(Map("username" -> comment.username, "comment" -> comment.comment)))))
      }
      case None => BadRequest("Couldn't map json!")
    }
  }

  def submitComment = Action { implicit req =>
    AsyncResult(
      (userCommentService ? SaveComment(userform.bindFromRequest.get))
        .map(result => Redirect(routes.Application.index())))
  }

  def lookupCompany = Action {
    Async {
      WS.url("http://maps.googleapis.com/maps/api/geocode/json?address=Goodge+Street&sensor=false").get()
        .map { response => Ok((response.json \ "results" \ "address_components").as[JsValue]) }
    }

  }

  def allComments = Action {
    Async(
      (userCommentService ? GetAllComments(true))
        .mapTo[JsArray].map(result => {
          Ok(Json.toJson(Map("existingComments" -> result)))
        }))

  }

}
