package com.barterbay.app;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class BarterBay {

  private static final StopWatch watchStartup = new StopWatch("Startup Api");

  public static void main(String[] args) {
    watchStartup.start("BarterBay Api Startup");
    var app = new SpringApplication(BarterBay.class);
    //DefaultProfileUtil.addDefaultProfile(app);
    ConfigurableApplicationContext context = app.run(args);
    var env = context.getEnvironment();
    watchStartup.stop();
    logApplicationStartup(env);
  }

  private static void logApplicationStartup(Environment env) {
    var protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    var serverPort = env.getProperty("server.port");
    var contextPath = env.getProperty("server.servlet.context-path");
    if (StringUtils.isBlank(contextPath)) {
      contextPath = "/";
    }
    var hostAddress = "localhost";
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.warn("The host name could not be determined, using `localhost` as fallback");
    }
    log.info(
      "\n----------------------------------------------------------\n\t"
        + "Application '{}' is running! Access URLs:\n\t"
        + "Local: \t\t{}://localhost:{}{}\n\t"
        + "External: \t{}://{}:{}{}\n\t"
        + "Profile(s): \t{}\n----------------------------------------------------------",
      env.getProperty("spring.application.name"),
      protocol,
      serverPort,
      contextPath,
      protocol,
      hostAddress,
      serverPort,
      contextPath,
      env.getActiveProfiles());
  }

}
