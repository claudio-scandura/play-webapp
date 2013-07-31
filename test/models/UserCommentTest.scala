package models

import org.junit.Test
import play.api.libs.json.Json
import org.slf4j.LoggerFactory
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.specs2.runner.JUnitRunner
import junit.framework.Assert._
import scala.collection.immutable.Seq

class UserCommentTest {
  
  val logger = LoggerFactory.getLogger(getClass())
  
  @Test
  def userCommentShouldBeTransformedIntoProperJson() {
    val comment = new UserComment("Maccio", "Whatever")
    val expected = "{\"username\":\"Maccio\",\"comment\":\"Whatever\"}"
    val json = Json.stringify(Json.toJson(comment))
    
    assertEquals(expected, json)
  }
  
  @Test
  def userCommentListShouldBeTransformedIntoProperJson() {
	  val comment1 = new UserComment("Maccio", "Whatever")
	  val comment2 = new UserComment("Ivo", "Indeed!")
	  val list = Seq(comment1, comment2)
	  val expected = "[{\"username\":\"Maccio\",\"comment\":\"Whatever\"},{\"username\":\"Ivo\",\"comment\":\"Indeed!\"}]"
	  val json = Json.stringify(Json.toJson(list))
	  
	  assertEquals(expected, json)
  }
  
  @Test
  def shouldConvertJsonToUserComment() {
    val input = "{\"username\":\"Maccio\",\"comment\":\"Whatever\"}"
    val result = UserComment.userCommentReads.reads(Json.parse(input)).get
    assertEquals("Maccio", result.username)
    assertEquals("Whatever", result.comment)
  }
  

}