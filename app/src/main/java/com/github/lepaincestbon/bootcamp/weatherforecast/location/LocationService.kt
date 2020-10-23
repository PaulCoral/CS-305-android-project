package com.github.lepaincestbon.bootcamp.weatherforecast.location

interface LocationService {
    fun getCurrentLocation() : Location?
}