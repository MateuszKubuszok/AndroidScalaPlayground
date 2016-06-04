package com.talkie.client.common

package object services {

  type ~@~>[From[_] <: Service[_], To[_]] = PartialServiceInterpreter[From, To]
  type ~&~>[From[_] <: Service[_], To[_]] = PartialServiceInterpreterCompanion[From, To]
}
