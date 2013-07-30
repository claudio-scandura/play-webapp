package models

import akka.actor.Actor
import java.sql.Connection
import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class UserComment(username: String, comment: String)

object UserComment {
  implicit val userCommentWrites = (
    (__ \ "username").write[String] and
    (__ \ "comment").write[String])(unlift(UserComment.unapply))

  implicit val userCommentReads = (
    (__ \ "username").read[String] and
    (__ \ "comment").read[String])(UserComment.apply _)

  //  Alternative (older) way to define the Writes 
  //  implicit val userWrites = new Writes[UserComment] {
  //    def writes(c: UserComment): JsValue = {
  //      Json.obj(
  //        "username" -> c.username,
  //        "comment" -> c.comment)
  //    }
  //  }
}

case class SaveComment(commen: UserComment)
case class GetAllComments(asJson: Boolean)

case class CommentList(comments: Seq[UserComment])

class UserCommentDAO extends Actor {

  def insert(userComment: UserComment)(implicit c: Connection) =
    SQL("insert into UserComment(username, comment) values ({username}, {comment})").on(
      "username" -> userComment.username,
      "comment" -> userComment.comment).executeInsert()

  def findAllComments()(implicit c: Connection) =
    SQL("select * from UserComment")
      .as(str("username") ~ str("comment") *)
      .map(flatten).map((UserComment.apply _).tupled)

  //This line allows to have the Application in scope (required to get the DB connection
  import play.api.Play.current
  def receive = {
    case SaveComment(comment) => {
      DB.withConnection {
        implicit conn =>
          insert(comment)
          sender ! "Success"
      }
    }
    case GetAllComments(false) => {
      DB.withConnection {
        implicit conn =>
          val list = new CommentList(findAllComments)
          sender ! list
      }
    }
    case GetAllComments(true) => {
      DB.withConnection {
        implicit conn =>
          sender ! (Json.toJson(findAllComments))
      }
    }
  }
}