package com.talkie.client.common.events

trait Event {

  type Details

  def getDetails: Details
}
