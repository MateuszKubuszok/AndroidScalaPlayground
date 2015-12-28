package com.talkie.client.core.events

trait Event {

  type Details

  def getDetails: Details
}
