package org.rapidpm.microservice.demo.rest;


import org.rapidpm.microservice.demo.service.Service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Sven Ruppert on 27.05.15.
 */
@Path("/test")
public class Resource {

  @Inject Service service;

  @GET()
  @Produces("text/plain")
  public String get() {
    return "Hello Rest World " + service.doWork();
  }


}
