import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Google extends Simulation {

  object TW {
    val tw =
      exec(http("insights")
          .get("/insights"))
          .pause(8)
          .exec(http("products")
          .get("/products"))
  }


  val httpConf = http
    .baseURL("https://www.thoughtworks.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate, sdch")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36")

  val scn = scenario("RecordedSimulation").exec(TW.tw)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
