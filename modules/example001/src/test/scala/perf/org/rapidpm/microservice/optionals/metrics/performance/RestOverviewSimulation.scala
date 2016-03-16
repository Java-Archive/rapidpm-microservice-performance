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
import org.rapidpm.microservice.Main
import org.rapidpm.microservice.Main.{deploy, stop}
import org.rapidpm.microservice.optionals.metrics.performance.Histogramms

class RestOverviewSimulation extends Simulation {

  val baseURL: String = "rest/metrics/performance/histogramms/"

  val httpConf = http
    .baseURL("http://" + "127.0.0.1" + ":" + Main.DEFAULT_REST_PORT + "/") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  val scn = scenario("Overview Test")
    .repeat(1000) {
      exec(http("request_0001").get(baseURL + Histogramms.LIST_ALL_HISTOGRAMMS))
      //      exec(http("request_0002").get(baseURL + Overview.LIST_ALL_HISTOGRAMMS))
    }


  //  setUp(scn.inject(atOnceUsers(100)).protocols(httpConf))
  setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))


  before {
    System.setProperty(Main.REST_HOST_PROPERTY, "127.0.0.1")
    System.setProperty(Main.SERVLET_HOST_PROPERTY, "127.0.0.1")

    println("Simulation is about to start!")
    println("Start MicroService")
    deploy()
    println("MicroService Started")
  }

  after {
    println("Stop MicroService")
    stop()
    println("Simulation is finished!")
  }

}

//http://0.0.0.0:7081/rest/metrics/performance/histogramms/listAllHistogramms