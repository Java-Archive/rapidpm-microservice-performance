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

package junit.org.rapidpm.microservice.demo;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.*;
import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.MainUndertow;
import org.rapidpm.microservice.demo.servlet.MessageServlet;

import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ServletTest {

  private static String url;
  private final String USER_AGENT = "Mozilla/5.0";

  @BeforeClass
  public static void beforeClass() throws Exception {
    final PortUtils portUtils = new PortUtils();
    final int restPort = portUtils.nextFreePortForTest();
    final int servletPort = portUtils.nextFreePortForTest();

    url = "http://127.0.0.1:" + servletPort
        + MainUndertow.MYAPP
        + MessageServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];

    System.setProperty(MainUndertow.REST_HOST_PROPERTY, "127.0.0.1");
    System.setProperty(MainUndertow.SERVLET_HOST_PROPERTY, "127.0.0.1");

    System.setProperty(MainUndertow.REST_PORT_PROPERTY, restPort + "");
    System.setProperty(MainUndertow.SERVLET_PORT_PROPERTY, servletPort + "");
  }

  @Before
  public void setUp() throws Exception {
    Main.deploy();
  }

  @After
  public void tearDown() throws Exception {
    Main.stop();
  }

  @Test
  public void testServletGetReq001() throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", USER_AGENT);

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    final StringBuffer response;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
      String inputLine;
      response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
    }
//    System.out.println("response = " + response);
    Assert.assertEquals("Hello World CDI Service", response.toString());
  }

  @Test
  public void testServletGetReq002() throws Exception {
    final Content returnContent = Request.Get(url).execute().returnContent();
    Assert.assertEquals("Hello World CDI Service", returnContent.asString());
//    Request.Post("http://targethost/login")
//        .bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
//        .execute().returnContent();

  }


  @Test
  public void testServletPostRequest() throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    //add reuqest header
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(urlParameters);
    wr.flush();
    wr.close();

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + urlParameters);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    Assert.assertEquals("Hello World CDI Service", response.toString());


  }


}
