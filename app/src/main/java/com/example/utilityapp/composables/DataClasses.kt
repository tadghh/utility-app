package com.example.utilityapp.composables

/**
 * Data class representing weather information fetched from the API.
 */
data class WeatherData(

	/**
	 *  Weather Coordinates
	 */
	val coord: Coord?,
	/**
	 *  Weather station information
	 */
	val weather: List<Weather>?,
	/**
	 *  Weather Station?
	 */
	val base: String?,
	/**
	 *  Unknown
	 */
	val main: Main?,
	/**
	 *  Fog
	 */
	val visibility: Int?,
	/**
	 *  Wind speed/direction
	 */
	val wind: Wind?,
	/**
	 *  Clouds status
	 */
	val clouds: Clouds?,
	/**
	 *  Unknown
	 */
	val dt: Long?,
	/**
	 *  Unknown
	 */
	val sys: Sys?,
	/**
	 *  Timezone
	 */
	val timezone: Int?,
	/**
	 *  Station id?
	 */
	val id: Long?,
	/**
	 *  Name?
	 */
	val name: String?,
	/**
	 *  Call of duty?
	 */
	val cod: Int?
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
	val cod: String?,
	val message: Int?,
	val cnt: Int?,
	val list: List<ForecastItem>?,
	val city: City?
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
	val id: Int?,
	val name: String?,
	val coord: Coord?, // Fixed typo in property name
	val country: String?,
	val population: Int?,
	val timezone: Int?,
	val sunrise: Long?,
	val sunset: Long?
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
 * @property dt_txt The timestamp in text format.
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
 * @property feels_like The "feels like" temperature.
 * @property temp_min The minimum temperature.
 * @property temp_max The maximum temperature.
 * @property pressure The atmospheric pressure.
 * @property humidity The relative humidity.
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
 * @property temp The temperature.
 * @property feels_like The "feels like" temperature.
 * @property temp_min The minimum temperature.
 * @property temp_max The maximum temperature.
 * @property pressure The atmospheric pressure.
 * @property sea_level The sea-level atmospheric pressure.
 * @property grnd_level The ground-level atmospheric pressure.
 * @property humidity The relative humidity.
 * @property temp_kf The temperature in Kelvin.
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
	val pod: String?,
)