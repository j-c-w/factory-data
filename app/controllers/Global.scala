package controllers

import java.io.File

import backend.scala.DataLoader

import scala.util.Random

/*
 * Created by Jackson Woodruff on 27/07/2014 
 * 
 */

object Global {
  lazy val randoms: Stream[Char] = new Random().alphanumeric

  def nextNRandoms(n: Int): Array[Char] =
    randoms.take(n).toArray

  def getSessionId =
    nextNRandoms(20)

  lazy val pictureFileLocation = new File("C:\\Users\\Jackson\\Projects\\IPA\\PlayTest\\HelloWorld\\public\\images\\gen")

  val baseData = DataLoader.dataAsList
}
