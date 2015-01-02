package controllers

import java.io._
import java.nio.file.{Files, Paths}

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
    nextNRandoms(40)

  lazy val pictureFileLocation = new File("C:\\Users\\Jackson\\Projects\\IPA\\PlayTest\\restore\\factory-data\\public\\images\\gen")
  lazy val errorPictureLocation = new File("C:\\Users\\Jackson\\Projects\\IPA\\PlayTest\\restore\\factory-data\\public\\images\\graphFailed.png")
  lazy val dataCVSLocation = new File("C:\\Users\\Jackson\\Projects\\IPA\\Data\\harmonized data\\second_harmonized.csv")

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
        println(formString)
        Some(Serialization.unserialize(formString))
      } catch {
        case ex: Exception => {
          println("Error restoring previous query, ID: " + queryId)
          println("Error Message: " + ex.getMessage)
          None
        }
      }
    }
  }

  def getPictureFile: File = {
    val saveDir = new File(pictureFileLocation + "/" + nextNRandoms(40).mkString("") + ".png")
    if (saveDir.exists) {
      getPictureFile
    } else {
      saveDir
    }
  }
}
