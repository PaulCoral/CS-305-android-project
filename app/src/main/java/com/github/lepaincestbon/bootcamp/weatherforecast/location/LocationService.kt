package com.github.lepaincestbon.bootcamp.weatherforecast.location

import java.util.function.Consumer

interface LocationService {
    fun getCurrentLocation() : Location?

    fun subscribeToLocationUpdates(consumer : Consumer<Location>) : Int

    fun unsubscribeToLocationUpdates(id : Int)
}