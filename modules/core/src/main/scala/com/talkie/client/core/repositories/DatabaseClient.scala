package com.talkie.client.core.repositories

import scala.slick.driver.SQLiteDriver.simple._

trait DatabaseClient {

  val db: Database
}

class DataBaseClientImpl(dbUrl: String, dbDriver: String) extends DatabaseClient {

  lazy val db = Database.forURL(dbUrl, dbDriver)
}
