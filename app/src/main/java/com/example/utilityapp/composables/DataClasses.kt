package com.example.utilityapp.composables

import com.google.gson.annotations.SerializedName

/**
 * Data class representing weather information fetched from the API.
 * @property coord Coordinates of the location.
 * @property weather List of weather conditions at the location.
 * @property base Information about the weather station.
 * @property main Main weather-related information such as temperature, pressure, etc.
 * @property visibility Visibility at the location.
 * @property wind Wind-related information including speed and direction.
 * @property clouds Cloud-related information.
 * @property dt Timestamp of the weather data.
 * @property sys System information related to the weather data.
 * @property timezone Timezone of the location.
 * @property id Unique identifier for the weather station.
 * @property name Name of the location.
 * @property cod HTTP response code from the API.
 */
data class WeatherData(
	val coord: Coord? = null,
	val weather: List<Weather>? = null,
	val base: String? = null,
	val main: Main? = null,
	val visibility: Int? = null,
	val wind: Wind? = null,
	val clouds: Clouds? = null,
	val dt: Long? = null,
	val sys: Sys? = null,
	val timezone: Int? = null,
	val id: Long? = null,
	val name: String? = null,
	val cod: Int? = null
)

/**
 * Data class representing forecast information fetched from the API.
 * @property cod The HTTP response code from the API.
 * @property message The message from the API.
 * @property cnt The number of forecast items.
 * @property list List of forecast items containing weather information.
 * @property city Details about the city for which the forecast is provided.
 */
data class ForecastData(
	val cod: String? = null,
	val message: Int? = null,
	val cnt: Int? = null,
	val list: List<ForecastItem>? = null,
	val city: City? = null
)

/**
 * Data class representing a city in the data fetched from the API.
 * @property id The unique identifier of the city.
 * @property name The name of the city.
 * @property coord Coordinates of the city (latitude and longitude).
 * @property country The country code of the city.
 * @property population The population of the city.
 * @property timezone The timezone of the city.
 * @property sunrise The timestamp of sunrise in the city.
 * @property sunset The timestamp of sunset in the city.
 */
data class City(
	val id: Int? = null,
	val name: String? = null,
	val coord: Coord? = null, // Fixed typo in property name
	val country: String? = null,
	val population: Int? = null,
	val timezone: Int? = null,
	val sunrise: Long? = null,
	val sunset: Long? = null
)

/**
 * Data class representing Forecast data for the next 5 days with 3-hour gaps.
 * @property dt The timestamp of the forecast data.
 * @property main Main weather information for the forecast.
 * @property weather List of weather conditions.
 * @property clouds Cloud information.
 * @property wind Wind information.
 * @property visibility The visibility in meters.
 * @property pop Probability of precipitation.
 * @property sys Additional system information for the forecast.
 * @property dtTxt The timestamp in text format.
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
	@SerializedName("dt_txt") val dtTxt: String
)

/**
 * Data class representing co-ordinates the data is fetched from.
 * @property lon The longitude coordinate.
 * @property lat The latitude coordinate.
 */
data class Coord(
	val lon: Double?,
	val lat: Double?
)

/**
 * Data class representing the weather entity.
 * @property id The unique identifier of the weather entity.
 * @property main The main weather condition (e.g., Clear, Rain).
 * @property description A detailed description of the weather condition.
 * @property icon The icon code representing the weather condition.
 */
data class Weather(
	val id: Int?,
	val main: String?,
	val description: String?,
	val icon: String?
)

/**
 * Data class representing the attributes for the weather like temperature, pressure etc.
 * @property temp The temperature.
 * @property feelsLike The "feels like" temperature.
 * @property tempMin The minimum temperature.
 * @property tempMax The maximum temperature.
 * @property pressure The atmospheric pressure.
 * @property humidity The relative humidity.
 */
data class Main(
	val temp: Double?,
	@SerializedName("feels_like") val feelsLike: Double?,
	@SerializedName("temp_min ") val tempMin: Double?,
	@SerializedName("temp_max") val tempMax: Double?,
	val pressure: Int?,
	val humidity: Int?
)

/**
 * Data class representing the attributes for the weather forecast like temperature, pressure etc.
 * @property temp The temperature.
 * @property feelsLike The "feels like" temperature.
 * @property tempMin The minimum temperature.
 * @property tempMax The maximum temperature.
 * @property pressure The atmospheric pressure.
 * @property seaLevel The sea-level atmospheric pressure.
 * @property groundLevel The ground-level atmospheric pressure.
 * @property humidity The relative humidity.
 * @property tempKF The temperature in Kelvin.
 */
data class MainForecast(
	val temp: Double?,
	val feelsLike: Double?,
	@SerializedName("temp_min") val tempMin: Double?,
	@SerializedName("temp_max") val tempMax: Double?,
	val pressure: Int?,
	@SerializedName("sea_level") val seaLevel: Int?,
	@SerializedName("grnd_level") val groundLevel: Int?,
	val humidity: Int?,
	@SerializedName("temp_kf") val tempKF: Double?
)

/**
 * Data class representing the wind data from the weather.
 * @property speed The wind speed.
 * @property deg The wind direction in degrees.
 * @property gust The wind gust speed.
 */
data class Wind(
	val speed: Double?,
	val deg: Int?,
	val gust: Double?
)

/**
 * Data class representing the entity to hold data for clouds.
 * @property all The cloudiness percentage.
 */
data class Clouds(
	val all: Int?
)

/**
 * Data class representing some geographical data from the API.
 * @property type The type of geographical data.
 * @property id The unique identifier of the geographical data.
 * @property country The country code.
 * @property sunrise The timestamp of sunrise.
 * @property sunset The timestamp of sunset.
 */
data class Sys(
	val type: Int?,
	val id: Int?,
	val country: String?,
	val sunrise: Long?,
	val sunset: Long?
)

/**
 * Data class to represent an internal entity from the API data.
 * @property pod The pod value.
 */
data class SysForecast(
	val pod: String?
)