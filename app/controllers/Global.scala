package controllers

import java.io._
import java.nio.file.{Files, Paths}
import java.text.SimpleDateFormat
import java.util.Calendar

import backend.scala.DataLoader
import frontend.Serialization

import scala.util.Random
/*
 * Created by Jackson Woodruff on 27/07/2014 
 * 
 */

object Global {
  def nextNRandoms(n: Int): Array[Char] =
    Random.alphanumeric.take(n).toArray

  def getQueryId =
    getDateStringWithoutDay + "-" + nextNRandoms(5).mkString("")

  def getPictureSaveString =
    nextNRandoms(20).mkString("")

  private lazy val pathToFiles = new File("").getAbsolutePath

  lazy val pictureFileLocation = new File(pathToFiles, "public\\images\\gen")
  lazy val errorPictureLocation = new File(pathToFiles, "public\\images\\graphFailed.png")
  lazy val dataCVSLocation = new File(pathToFiles, "conf\\private\\fourth_harmonized.csv")

  lazy val baseData = DataLoader.dataAsList

  def sendNotification(queryId: String, formData: Option[Map[String, Seq[String]]]): Unit = {
    val location = new File(pictureFileLocation + "/" + queryId)
    location.createNewFile()
    val writer = new PrintWriter(location)
    writer.write(Serialization.serialize(formData.getOrElse(Map())))
    writer.close()
  }

  /*
   * Please note that if you pass this an empty string it will crash
   */
  def restoreSession(queryId: String): Option[Map[String, Seq[String]]] = {
    val location = new File(pictureFileLocation + "/" + queryId)
    if (!location.exists()) {
      None
    } else {
      try {
        val formString = new String(Files.readAllBytes(Paths.get(location.toString)))
        val serialized = Some(Serialization.unserialize(formString))
        println("Unserialization successful")
        updateDate(location, serialized.get)
        serialized
      } catch {
        case ex: Exception => {
          println("Error restoring previous query, ID: " + queryId)
          println("Error Message: " + ex.getMessage)
          None
        }
      }
    }
  }
  
  /*
   * This method takes the unserilized form and
   * just reserialises it with the new date.
   */
  private def updateDate(location: File, currentContents: Map[String, Seq[String]]): Unit = {
    val finalContents = Serialization.serialize(currentContents)
    val writer = new PrintWriter(location)
    writer.write(finalContents)
    writer.close()
  }

  def getDateStringWithoutDay =
    new SimpleDateFormat("MM-yy")
      .format(Calendar.getInstance.getTime)

  def getDateString =
    new SimpleDateFormat("dd/MM/yyyy")
      .format(Calendar.getInstance.getTime)

  def getPictureFile: File = {
    val saveDir = new File(pictureFileLocation + "/" + nextNRandoms(40).mkString("") + ".png")
    if (saveDir.exists) {
      getPictureFile
    } else {
      saveDir
    }
  }
}
