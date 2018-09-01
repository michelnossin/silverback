package com.nossin.silverback

import scala.meta.{Lit,Defn, Term, abort, _}

class MeasureTime(metricIdentifier: String, metricOutputFunction: Option[(String,Long,Long,Long) => Unit]) extends scala.annotation.StaticAnnotation {

  def this(metricIdentifier: String) {
    this(metricIdentifier,None)
  }

  inline def apply(defn: Any): Any = meta {

    defn match {
      case defn: Defn.Def =>
        this match {
          case q"new $_($metricName)" =>
            val body: Term = MeasureTimeMacroImpl.expand(metricName, defn)

            defn.copy(body = body)
          case q"new $_($metricName,$outputFunction)" =>
            val body: Term = MeasureTimeMacroImpl.expand(metricName, outputFunction, defn)

            defn.copy(body = body)
          case x =>
            abort(s"Unrecognized pattern $x")
        }

      case _ =>
        abort("This annotation only works on `def`")
    }
  }
}

object MeasureTimeMacroImpl {
  /**
    *
    * @param metricNameExpr     - Argument pass to `metricTime` macro, should be type of `String`
    * @param annotatedDef       - Methods that is annotated
    */
    def expand(metricNameExpr: Term.Arg,annotatedDef: Defn.Def): Term = {

      val metricName = Term.Name(metricNameExpr.syntax)

      annotatedDef match {
        case q"..$_ def $methodName(..$nonCurriedParams): $rtType = $expr" =>
          q"""
                println("Time measurement for metric : "+ $metricName )
                val startTime = _root_.java.lang.System.currentTimeMillis()
                val result = ${annotatedDef.body}
                val endTime = _root_.java.lang.System.currentTimeMillis()
                val elapsed = endTime - startTime
                println("Start,- and End-time : " + startTime + " and " + endTime)
                println("Elapsed time : " + elapsed + "ms!!")
                result
                """

        case other => abort(s"Expected non-curried method, got $other")
      }
  }

  /**
    *
    * @param outputFunctionExpr - Argument pass to `metricTime` macro, should be type of `Option[(Int,Int,Int) => None]`
    * @param metricNameExpr     - Argument pass to `metricTime` macro, should be type of `String`
    * @param annotatedDef       - Methods that is annotated
    */
  def expand(metricNameExpr: Term.Arg, outputFunctionExpr : Term.Arg,annotatedDef: Defn.Def): Term = {

    val metricName = Term.Name(metricNameExpr.syntax)
    val outputFunction = Term.Name(outputFunctionExpr.syntax)

    annotatedDef match {
      case q"..$_ def $methodName(..$nonCurriedParams): $rtType = $expr" =>
        q"""

                val startTime = _root_.java.lang.System.currentTimeMillis()
                val result = ${annotatedDef.body}
                val endTime = _root_.java.lang.System.currentTimeMillis()
                val elapsed = endTime - startTime
                ${outputFunction}($metricName,startTime,endTime,elapsed)
                result
                """

      case other => abort(s"Expected non-curried method, got $other")
    }
  }
}