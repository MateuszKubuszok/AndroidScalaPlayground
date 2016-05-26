package com.talkie.client.core.services

import scalaz.concurrent.Task
import scalaz.~>

trait Service[R]

trait ServiceInterpreter extends (Service ~> Task)
