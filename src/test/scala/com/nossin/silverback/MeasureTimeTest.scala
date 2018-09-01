package com.nossin.silverback

import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}

class MeasureTimeTest extends FunSuite with Matchers with BeforeAndAfterAll  {

  private var someResultList : List[Long] = List()

  private def printStats(metricsName : String,startTime : Long,endTime : Long,elapsedMsTime : Long): Unit = {
    println("Custom output for metric " + metricsName + ": " + startTime + ", " + endTime + "," + elapsedMsTime + " ms")
  }

  private def storeStats(metricsName : String,startTime : Long,endTime : Long,elapsedMsTime : Long): Unit = {
    println("Saving metrics for " + metricsName + ": " + startTime + ", " + endTime + "," + elapsedMsTime + " ms")
    this.someResultList = this.someResultList :+ elapsedMsTime
  }

  @MeasureTime("plusOne")
  private def plusOne (x : Int) : Int = {
    Thread.sleep(500)
    x + 1
  }

  @MeasureTime("michelfunction",printStats)
  private def callThis(x : Int) : Int = {
    Thread.sleep(x)
    x
  }

  @MeasureTime("willstore", storeStats)
  private def willStore(x: Int): Unit = {
    Thread.sleep(500)

  }

  override def beforeAll(): Unit = {
  }

  override def afterAll(): Unit = {

  }

  test("Check if we can measure without output function") {
    callThis(400) should equal (400)
    callThis(500) should equal (500)
  }

  test("Check if we can measure with custom output function") {
    plusOne(4) should equal (5)
  }

  test("Check if we get the measurements") {
    willStore(300)
    willStore(400)
    this.someResultList.size should equal (2)
  }
}