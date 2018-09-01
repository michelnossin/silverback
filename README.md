# Silverback

An extremely simple to use Decorator style Scala function measure tool. 

## Introduction 

Measure, profile and monitor your Scala functions and methods using Annotations.
This will give Python like decorator capabilities enabling you to monitor the performance of your functions without altering your code.

This project will also give you the possibility to add a custom function in which you can define whatever you want to do with the metrics. For example print the metrics, store them in a database or datalake, or send them to your monitor system.

This project has been tested with Spark structured streaming. You can decorate the functions used by your udf's . For each microbatch or call metrics will be processed.

## Installation
```
Build the jar using Sbt build.
Move the jar into your classpath. 
 
Soon we will try to get this into maven so you can use SBT dependencies.
```

## Example usage without custom function, will provide some default text in Standard output

```
@MeasureTime("plusOne")
  private def plusOne (x : Int) : Int = {
    Thread.sleep(500)
    x + 1
  }
  
val x = plusOne(4)

Time measurement for metric : plusOne
Start,- and End-time : 1535827642351 and 1535827642851
Elapsed time : 500ms!!
```

## Example usage with custom function . In this example we will just print, but you can do anything eg store in a database or send events towards your monitor system.
``` 
(MetricName : String,startTime : Long, endTime : Long,elapsedMsTime : Long) => Unit

private def printStats(metricsName : String,startTime : Long,endTime : Long,elapsedMsTime : Long): Unit = {
    println("Custom output for metric " + metricsName + ": " + startTime + ", " + endTime + "," + elapsedMsTime + " ms")
}

@MeasureTime("michelfunction",printStats)
  private def callThis(x : Int) : Int = {
    Thread.sleep(x)
    x
  }
 
val x = callThis(400)
val y = callThis(500)

Custom output for metric michelfunction: 1535827641411, 1535827641812,401 ms
Custom output for metric michelfunction: 1535827641833, 1535827642334,501 ms

```
