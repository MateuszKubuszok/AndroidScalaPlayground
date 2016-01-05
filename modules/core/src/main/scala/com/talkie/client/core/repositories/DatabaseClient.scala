package com.talkie.client.core.repositories

import android.app.Activity

import scala.slick.driver.SQLiteDriver.simple._

trait DatabaseClient {

  val db: Database
}

trait DatabaseClientComponent {

  def databaseClient: DatabaseClient
}

trait DatabaseClientComponentImpl extends DatabaseClientComponent {
  self: Activity =>

  lazy val dbUrl = s"jdbc:sqlite:${getApplicationContext.getFilesDir}talkieDatabase.sqlite"

  lazy val dbDriver = "org.sqldroid.SQLDroidDriver"

  object databaseClient extends DatabaseClient {

    lazy val db = Database.forURL(dbUrl, driver = "org.sqldroid.SQLDroidDriver")
  }
}
