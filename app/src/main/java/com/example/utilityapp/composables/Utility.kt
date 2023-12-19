package com.example.utilityapp.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Converts the notes into an observable type.
 *
 * @param T The type of data in the LiveData.
 * @param initial The initial value of the LiveData.
 * @return The state as a Composable.
 */
@Composable
fun <T> LiveData<T>.observeAsState(initial: T): T {
	val liveData = this
	val state = remember { mutableStateOf(initial) }

	DisposableEffect(liveData) {
		val observer = Observer<T> { value ->
			state.value = value
		}
		liveData.observeForever(observer)

		onDispose {
			liveData.removeObserver(observer)
		}
	}

	return state.value
}


/**
 * Parses the JSON data into a [WeatherData] object.
 *
 * @param jsonData JSON data in string format to be parsed.
 * @return Parsed [WeatherData] object.
 */
fun parseJsonToWeatherData(jsonData: String): WeatherData {
	return Gson().fromJson(jsonData, WeatherData::class.java)
}

/**
 * Parses the JSON data into a [ForecastData] object.
 *
 * @param jsonData JSON data in string format to be parsed.
 * @return Parsed [ForecastData] object.
 */
fun parseJsonToForecastData(jsonData: String): ForecastData {
	return Gson().fromJson(jsonData, ForecastData::class.java)
}


/**
 * Formats the provided date-time string into a 12-hour format.
 *
 * @param dateTime The date-time string to be formatted.
 * @return The formatted time string in 12-hour format.
 */
fun getFormattedTime(dateTime: String): String {
	val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
	val outputFormat = SimpleDateFormat("h a", Locale.getDefault())
	val date = inputFormat.parse(dateTime)
	return outputFormat.format(date!!)
}

/**
 * Formats the provided date string into a custom date format.
 *
 * @param date The date string to be formatted.
 * @return The formatted date string in a custom format (day with suffix and month).
 */
fun getFormattedDate(date: String): String {
	val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
	val outputFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
	val parsedDate = inputFormat.parse(date)

	val calendar = Calendar.getInstance()
	calendar.time = parsedDate!!
	val day = calendar.get(Calendar.DAY_OF_MONTH)
	val suffix = getDayOfMonthSuffix(day)

	return outputFormat.format(parsedDate).replace(day.toString(), "$day$suffix")
}

/**
 * Determines the suffix for a given day of the month (e.g., 1st, 2nd, 3rd, etc.).
 *
 * @param n The day of the month.
 * @return The suffix for the provided day.
 */
fun getDayOfMonthSuffix(n: Int): String {
	return when (n % 10) {
		1 -> if (n == 11) "th" else "st"
		2 -> if (n == 12) "th" else "nd"
		3 -> if (n == 13) "th" else "rd"
		else -> "th"
	}
}