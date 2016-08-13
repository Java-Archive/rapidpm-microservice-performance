package perf.org.rapidpm.microservice.optionals.metrics.performance

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import org.rapidpm.microservice.{Main, MainUndertow}
import org.rapidpm.microservice.MainUndertow.DEFAULT_REST_PORT
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

  val baseURLRestTest: String = "rest/test/"

  val httpConfRestTest = http
    .baseURL("http://" + "127.0.0.1" + ":" + Main.DEFAULT_REST_PORT + "/") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  val scnRestTest = scenario("RestTest 001")
    .repeat(1000) {
    exec(http("RestTest")
      .get(baseURLRestTest)
      .check(status.is(200))
    )
  }


  val baseURL: String = "rest/metrics/performance/histogramms/"

  val httpConf = http
    .baseURL("http://" + "127.0.0.1" + ":" + MainUndertow.DEFAULT_REST_PORT + "/") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  val scn = scenario("HistogrammRestTest")
    .repeat(1000) {
    exec(http("HistogrammRestTest")
      .get(baseURL + Histogramms.LIST_ALL_HISTOGRAMMS)
      .check(status.is(200))
    )
  }


    setUp(
      scn.inject(atOnceUsers(100)).protocols(httpConf),
      scnRestTest.inject(atOnceUsers(100)).protocols(httpConfRestTest)
    )
}
