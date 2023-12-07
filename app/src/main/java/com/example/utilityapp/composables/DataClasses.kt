package com.example.utilityapp.composables

/**
 * Data class representing weather information fetched from the API.
 */
data class WeatherData(
    val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    val main: Main?,
    val visibility: Int?,
    val wind: Wind?,
    val clouds: Clouds?,
    val dt: Long?,
    val sys: Sys?,
    val timezone: Int?,
    val id: Long?,
    val name: String?,
    val cod: Int?
)

/**
 * Data class representing forecast information fetched from the API.
 */
data class ForecastData(
    val cod: String?,
    val message: Int?,
    val cnt: Int?,
    val list: List<ForecastItem>?,
    val city: City?
)

/**
 * Data class representing a city in the data fetched from the API.
 */
data class City(
    val id: Int?,
    val name: String?,
    val coordval: Coord?,
    val country: String?,
    val population: Int?,
    val timezone: Int?,
    val sunrise: Long?,
    val sunset: Long?
)

/**
 * Data class representing Forecast data for the next 5 days with 3-hour gaps
 */
data class ForecastItem(
    val dt: Long,
    val main: MainForecast?,
    val weather: List<Weather>?,
    val clouds: Clouds?,
    val wind: Wind?,
    val visibility: Int?,
    val pop: Float?,
    val sys: SysForecast?,
    val dt_txt: String,
)

/**
 * Data class representing co-ordinates the data is fetched from
 */
data class Coord(
    val lon: Double?,
    val lat: Double?
)

/**
 * Data class representing the weather entity
 */
data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

/**
 * Data class representing the attributes for the weather like temperature, pressure etc.
 */
data class Main(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val humidity: Int?
)

/**
 * Data class representing the attributes for the weather forecast like temperature, pressure etc.
 */
data class MainForecast(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val sea_level: Int?,
    val grnd_level: Int?,
    val humidity: Int?,
    val temp_kf: Double?,
)

/**
 * Data class representing the wind data from the weather
 */
data class Wind(
    val speed: Double?,
    val deg: Int?,
    val gust: Double?
)

/**
 * Data class representing the entity to hold data for clouds
 */
data class Clouds(
    val all: Int?
)

/**
 * Data class representing some geographical data from the API
 */
data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)

/**
 * Data class to represent an internal entity from the API data
 */
data class SysForecast(
    val pod: String?,
)