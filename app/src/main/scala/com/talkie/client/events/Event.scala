package com.talkie.client.events

trait Event {

  type Details

  def getDetails: Details
}
