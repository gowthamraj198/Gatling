import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Search extends Simulation {

  val csvFeeder = csv("data.csv").circular

  object Search {
    val search =
      exec(http("add new")
        .get("/computers/new"))
        .pause(8)
        .feed(csvFeeder)
        .exec(http("go to home")
          .post("/computers")
          .formParam("name", "${name}")
          .formParam("introduced", "${introduced}")
          .formParam("discontinued", "")
          .formParam("company", ""))
        .pause(10)
        .exec(http("search for springboot")
          .get("/computers?f=springboot"))
  }


  val httpConf = http
    .baseURL("http://computer-database.gatling.io")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate, sdch")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36")

  val scn = scenario("RecordedSimulation").exec(Search.search)

  setUp(scn.inject(atOnceUsers(5))).protocols(httpConf).assertions(global.responseTime.mean.lt(600),
    forAll.responseTime.max.lt(600), forAll.allRequests.percent.lt(500))
}
