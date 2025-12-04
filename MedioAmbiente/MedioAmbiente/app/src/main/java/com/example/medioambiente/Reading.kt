package com.example.medioambiente

data class Reading(
    var id: Long,
    var timestamp: Long,
    var temperature: Double?,
    var humidity: Double?,
    var pressure: Double?,
    var light: Double?
)
