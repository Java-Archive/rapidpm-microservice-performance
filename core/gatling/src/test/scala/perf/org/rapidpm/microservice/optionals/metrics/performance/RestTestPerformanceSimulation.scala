package perf.org.rapidpm.microservice.optionals.metrics.performance

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import org.rapidpm.ddi.DI
import org.rapidpm.dependencies.core.net.PortUtils
import org.rapidpm.microservice.{Main, MainUndertow}
import org.rapidpm.microservice.optionals.metrics.performance.Histogramms

/**
  * Copyright (C) 2010 RapidPM
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  * http://www.apache.org/licenses/LICENSE-2.0
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *
  * Created by RapidPM - Team on 08.06.16.
  */
class RestTestPerformanceSimulation extends Simulation {

  val port: String = "%d".format(new PortUtils().nextFreePortForTest())

  val baseURLRestTest: String = "rest/test/"

  val httpConfRestTest = http
    .baseURL("http://" + "127.0.0.1" + ":" + port + "/") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
  val scnRestTest = scenario("RestTest 001")
    .repeat(1000) {
      exec(http("RestTest")
        .get(baseURLRestTest)
        .check(status.is(200))
      )
    }


  val baseURL: String = "rest/metrics/performance/histogramms/"

  var httpConf = http
  .baseURL("http://" + "127.0.0.1" + ":" + port + "/")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")

  val scn = scenario("HistogrammRestTest")
    .repeat(100) {
      exec(http("HistogrammRestTest")
        .get(baseURL + Histogramms.LIST_ALL_HISTOGRAMMS)
        .check(status.is(200))
      )
    }


  setUp(
    scn.inject(atOnceUsers(100)).protocols(httpConf),
    scnRestTest.inject(atOnceUsers(100)).protocols(httpConfRestTest)
  )

  before {
    //port = System.getProperty(MainUndertow.REST_PORT_PROPERTY)
    System.setProperty(MainUndertow.REST_HOST_PROPERTY, "127.0.0.1")
    System.setProperty(MainUndertow.SERVLET_HOST_PROPERTY, "127.0.0.1")
    //
    System.setProperty(MainUndertow.REST_PORT_PROPERTY, port)

    DI.clearReflectionModel()
    DI.bootstrap()
//    DI.activatePackages("org.rapidpm")
//    DI.activatePackages("junit.org.rapidpm")
//    DI.activatePackages("perf.org.rapidpm")
    println("Simulation is about to start!")
    println("Start MicroService")
    Main.deploy()
    println("MicroService Started")
  }

  after {
    println("Stop MicroService")
        Main.stop()
        DI.clearReflectionModel()
        println("Simulation is finished!")
  }
}
