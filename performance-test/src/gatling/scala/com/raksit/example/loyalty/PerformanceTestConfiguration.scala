package com.raksit.example.loyalty

import io.gatling.core.Predef._ // required for Gatling core structure DSL
import io.gatling.http.Predef._ // required for Gatling HTTP DSL
import io.gatling.http.protocol.HttpProtocolBuilder

object PerformanceTestConfiguration {

  private val serviceUrl = System.getProperty("service.url", "http://localhost:8080")
  val httpConfiguration: HttpProtocolBuilder = http.baseUrl(serviceUrl)
}
