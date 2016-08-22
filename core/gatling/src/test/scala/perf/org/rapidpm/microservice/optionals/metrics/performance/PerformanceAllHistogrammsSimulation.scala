/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package perf.org.rapidpm.microservice.optionals.metrics.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.rapidpm.ddi.DI
import org.rapidpm.dependencies.core.net.PortUtils
import org.rapidpm.microservice.optionals.metrics.performance.Histogramms
import org.rapidpm.microservice.{Main, MainUndertow}

class PerformanceAllHistogrammsSimulation extends Simulation {

  val baseURL: String = "rest/metrics/performance/histogramms/"
  val port: String = "%d".format(new PortUtils().nextFreePortForTest())


  val httpConf: HttpProtocolBuilder = http
    .baseURL("http://" + "127.0.0.1" + ":" + port + "/") // Here is the root for all relative URLs
    .acceptEncodingHeader("gzip, deflate")

  val scn = scenario("Overview Test")
    //    .repeat(100000) {
    .repeat(100) {
    exec(http(this.getClass.getSimpleName)
      .get(baseURL + Histogramms.LIST_ALL_HISTOGRAMMS)
      .check(status.is(200))
    )
    //      exec(http("request_0002").get(baseURL + Overview.LIST_ALL_HISTOGRAMMS))
  }


  //  setUp(scn.inject(atOnceUsers(100)).protocols(httpConf))
  setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))


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

