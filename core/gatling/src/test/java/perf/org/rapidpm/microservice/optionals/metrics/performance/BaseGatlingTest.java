package perf.org.rapidpm.microservice.optionals.metrics.performance;

import org.junit.After;
import org.junit.Before;
import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.MainUndertow;

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
 * Created by RapidPM - Team on 20.08.16.
 */
public class BaseGatlingTest {

  @Before
  public void setUp() throws Exception {
    DI.clearReflectionModel();
    DI.activatePackages("org.rapidpm");
    DI.activatePackages("junit.org.rapidpm");
    DI.activatePackages("perf.org.rapidpm");
    System.setProperty(MainUndertow.REST_PORT_PROPERTY, new PortUtils().nextFreePortForTest() + "");
    Main.deploy();
  }

  @After
  public void tearDown() throws Exception {
    Main.stop();
    DI.clearReflectionModel();
  }


  protected String[] createBasicArgs(final String className) {
    final String[] args = new String[4];
    args[0] = "--results-folder"; //TODO global Constants
    args[1] = "target/gatling/"; //TODO global Constants

//    args1[2] = "--simulations-folder";
//    args1[3] = "src/test/scala";

//    args1[2] = "--simulation";
//    args1[3] = RestOverviewSimulation.class.getName();
//    args1[3] = "perf.org.rapidpm.microservice.optionals.metrics.performance.PerformanceAllHistogrammsSimulation";
    args[2] = "--simulation";
    args[3] = className;
    return args;
  }

}
