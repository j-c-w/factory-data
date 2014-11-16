package controllers

import java.io.File

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

  lazy val pictureFileLocation = new File("C:\\Users\\Jackson\\Projects\\IPA\\PlayTest\\factory-data\\public\\images\\gen")

  val baseData = DataLoader.dataAsList

  def sendNotification(sessionId: String): Unit = {
    val location = new File(pictureFileLocation + "/" + sessionId)
    location.createNewFile();
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
