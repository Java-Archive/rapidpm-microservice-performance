package perf.org.rapidpm.microservice.optionals.metrics.performance;

import io.gatling.app.Gatling;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rapidpm.ddi.DI;
import org.rapidpm.microservice.Main;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

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
public class StartServicesTest {


  @Before
  public void setUp() throws Exception {
    DI.clearReflectionModel();
    DI.activatePackages("org.rapidpm");
    DI.activatePackages("junit.org.rapidpm");
    DI.activatePackages("perf.org.rapidpm");

    Main.deploy();
  }

  @After
  public void tearDown() throws Exception {
    DI.clearReflectionModel();
  }

  @Test
  public void test001() throws Exception {
    final String[] args1 = new String[4];
    args1[0] = "--results-folder";
    args1[1] = "target/gatling/";

//    args1[2] = "--simulations-folder";
//    args1[3] = "src/test/scala";


//    args1[2] = "--simulation";
//    args1[3] = RestOverviewSimulation.class.getName();
//    args1[3] = "perf.org.rapidpm.microservice.optionals.metrics.performance.PerformanceAllHistogrammsSimulation";
    args1[2] = "--simulation";
    args1[3] = "perf.org.rapidpm.microservice.optionals.metrics.performance.RestTestPerformanceSimulation";

    Gatling.main(args1);
    Assert.assertFalse(false);
  }
}