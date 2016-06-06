package com.talkie.client.common

package object services {

  type ~@~>[From[T] <: Service[T], To[_]] = PartialServiceInterpreter[From, To]
  type ~&~>[From[T] <: Service[T], To[_]] = PartialServiceInterpreterCompanion[From, To]
}
