package sampleapplication;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class SampleApplication extends Simulation {

    // Http configuration
private HttpProtocolBuilder httpProtocol = http
.baseUrl("https://bejjjfz4jl.execute-api.eu-west-3.amazonaws.com/api")
.contentTypeHeader("application/x-www-form-urlencoded");


private static ChainBuilder authenticate =
exec(http("Authenticate").post("/login").formParam("username", "admin").formParam("password", "admin").check(jmesPath("accessToken").saveAs("token")));

private static ChainBuilder ping =
exec(http("Ping").post("/ping").header("Authorization", "#{token}"));


// Add the ScenarioBuilder:
ScenarioBuilder myScenario = scenario("My Scenario")
.exec(authenticate)
.pause(2).exec(ping);

// Add the setUp block:
{
    setUp(
        myScenario.injectOpen(atOnceUsers(1))
    ).protocols(httpProtocol).assertions(global().responseTime().max().lt(800), global().successfulRequests().percent().is(100.0));
  }

}
