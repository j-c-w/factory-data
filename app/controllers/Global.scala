package controllers

import java.io._
import java.nio.file.{Files, Paths}

import backend.scala.DataLoader

import scala.util.Random

/*
 * Created by Jackson Woodruff on 27/07/2014 
 * 
 */

object Global {
  def nextNRandoms(n: Int): Array[Char] =
    Random.alphanumeric.take(n).toArray

  def getSessionId =
    nextNRandoms(20)

  lazy val pictureFileLocation = new File("C:\\Users\\Jackson\\Projects\\IPA\\PlayTest\\restore\\factory-data\\public\\images\\gen")

  val baseData = DataLoader.dataAsList

  def sendNotification(sessionId: String, formData: Option[Map[String, Seq[String]]]): Unit = {
    val location = new File(pictureFileLocation + "/" + sessionId)
    location.createNewFile()
    val objectStream = new ObjectOutputStream(new FileOutputStream(location))
    objectStream.writeObject(formData)
    objectStream.close()
  }

  def restoreSession(sessionId: String): Option[Map[String, Seq[String]]] = {
    val location = new File(pictureFileLocation + "/" + sessionId)
    if (!location.exists()) {
      None
    } else {
      //get the map out of a pickle
      val objectInput = new ObjectInputStream(new FileInputStream(location))
      val map = objectInput.readObject()
      try {
        map.asInstanceOf
      } catch {
        case ex: Exception => {
          println("Error restoring previous query, ID: " + sessionId)
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
