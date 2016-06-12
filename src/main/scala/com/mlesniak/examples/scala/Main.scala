package com.mlesniak.examples.scala

import java.util

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

/**
  * Minimal viable spring/scala example with automatic conversion from deep scala maps to deep java maps.
  *
  * @author Michael Lesniak (mlesniak@micromata.de)
  */
object Main extends App {
  SpringApplication.run(classOf[MainConfig])
}

@RestController
@SpringBootApplication
class MainConfig {
  @RequestMapping(Array("/"))
  def index: java.util.Map[String, Any] = {
    Map("123" -> "456", "deeper" -> Map("456" -> "123", "foo" -> 23.23), "nicer" -> 1234)
  }

  /**
    * Implicit deep conversion from scala to java maps for serialization in Spring.
    *
    * @param m source map
    * @return java map
    */
  implicit def toJava(m: Map[String, Any]): java.util.Map[String, Any] = {
    val newMap = new util.HashMap[String, Any]()
    m.foreach {
      case (k, v) => {
        val mapValue =
          v match {
            case value: Map[String, Any] => toJava(value)
            case _ => v
          }
        newMap.put(k, mapValue)
      }
    }
    newMap
  }
}

