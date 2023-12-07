package com.example.utilityapp.composables.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.utilityapp.R
import androidx.compose.material.Divider
import com.example.utilityapp.composables.ForecastData
import com.example.utilityapp.composables.ForecastItem
import com.example.utilityapp.composables.WeatherData
import com.example.utilityapp.composables.getFormattedDate
import com.example.utilityapp.composables.getFormattedTime
import com.example.utilityapp.composables.parseJsonToForecastData
import com.example.utilityapp.composables.parseJsonToWeatherData


/**
 * Composable that represents the tab for displaying weather data.
 */
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherTab() {
    var selectedWeatherInterval by remember { mutableStateOf("Current Weather") }
    var weatherData by remember {
        mutableStateOf(
            WeatherData(
                null, null, null, null,
                null, null, null, null, null, null, null, null, null
            )
        )
    }
    var forecastData by remember { mutableStateOf(ForecastData(null, null, null, null, null)) }

    val apiKey = "a66838394baf9c9ddf43532a3e3377c1"
    val baseUrl = "https://api.openweathermap.org/data/2.5"
    val weatherDataTypes = listOf("Current Weather", "Weather forecast for the next 5 days")

    // Populate data variables by calling API on load
    LaunchedEffect(Unit)
    {

            val currentWeatherUrl =
                "$baseUrl/weather?lat=49.895138&lon=-97.138374&appid=$apiKey&units=metric"
            fetchWeatherData(currentWeatherUrl, object : WeatherDataCallback {
                override fun onSuccess(data: String) {
                    // Handle the successful response here
                    weatherData = parseJsonToWeatherData(data)

                }

                override fun onFailure(error: String) {
                    // Handle the failure here
                    println("Error: $error")

                }
            })

            val forecastUrl =
                "$baseUrl/forecast?lat=49.895138&lon=-97.138374&appid=$apiKey&units=metric"
            fetchWeatherData(forecastUrl, object : WeatherDataCallback {
                override fun onSuccess(data: String) {
                    // Handle the successful response here
                    forecastData = parseJsonToForecastData(data)

                }

                override fun onFailure(error: String) {
                    // Handle the failure here
                    println("Error: $error")
                }
            })

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Weather App",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        var expandedCatDropdown by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedCatDropdown,
            onExpandedChange = {
                expandedCatDropdown = !expandedCatDropdown
            }
        ) {

            TextField(
                value = selectedWeatherInterval,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) },
                modifier = Modifier.fillMaxWidth()
            )



            DropdownMenu(
                expanded = expandedCatDropdown,
                onDismissRequest = { expandedCatDropdown = false }
            ) {
                weatherDataTypes.forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            selectedWeatherInterval = type
                            expandedCatDropdown = false
                        }
                    ) {
                        Text(text = type)
                    }
                }
            }


        }





        if (selectedWeatherInterval == "Current Weather") {
            if (weatherData.weather != null) {
                DisplayWeather(weatherData)
            }
        } else {
            val forecastInfo = forecastData.list
            if (forecastInfo != null) {
                DisplayForecast(forecastInfo.groupBy { it.dt_txt.split(" ")[0] })
            }
        }


        // Display the selected weather interval
        Text(
            text = "Selected Interval: $selectedWeatherInterval",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}


/**
 * A composable function to display weather-related information in a card layout.
 *
 * @param title The title for the weather card.
 * @param value The value to be displayed.
 * @param icon The icon for the weather card.
 */
@Composable
fun WeatherCard(title: String, value: String, icon: Painter) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(40.dp)
                    .padding(0.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * A composable function to display forecasted weather information.
 *
 * @param weatherData A map containing forecasted weather items grouped by date.
 */
@Composable
fun DisplayWeather(weatherData: WeatherData) {
    val wbSunnyPainter: Painter = painterResource(id = R.drawable.wb_sunny)
    val locationOnPainter: Painter = painterResource(id = R.drawable.location_on)
    val trendingUpPainter: Painter = painterResource(id = R.drawable.trending_up)
    val wavesPainter: Painter = painterResource(id = R.drawable.waves)
    val waterPainter: Painter = painterResource(id = R.drawable.water)
    val windPainter: Painter = painterResource(id = R.drawable.wind)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item("City") { WeatherCard("City", weatherData.name.toString(), locationOnPainter) }
        item("Temperature") {
            WeatherCard(
                "Temperature",
                "${weatherData.main?.temp} °C",
                wbSunnyPainter
            )
        }
        item("Description") {
            WeatherCard(
                "Description",
                weatherData.weather?.firstOrNull()?.description ?: "N/A",
                trendingUpPainter
            )
        }
        item("Pressure") {
            WeatherCard(
                "Pressure",
                "${weatherData.main?.pressure} hPa",
                wavesPainter
            )
        }
        item("Humidity") { WeatherCard("Humidity", "${weatherData.main?.humidity}%", waterPainter) }
        item("Wind Speed") {
            WeatherCard(
                "Wind Speed",
                "${weatherData.wind?.speed} m/s",
                windPainter
            )
        }
        // Add more items for other relevant weather information similarly
    }
}

/**
 * A composable function to display forecasted weather information.
 *
 * @param forecastItems A map containing forecasted weather items grouped by date.
 */
@Composable
fun DisplayForecast(forecastItems: Map<String, List<ForecastItem>>) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        forecastItems.forEach { (date, forecastList) ->
            println(date)
            WeatherForecastCard(date = date, forecastList = forecastList)
        }
    }
}

/**
 * A composable function to display weather forecast for a specific date.
 *
 * @param date The date for the weather forecast.
 * @param forecastList List of forecast items for the specified date.
 */
@Composable
fun WeatherForecastCard(date: String, forecastList: List<ForecastItem>) {
    // Display a single day's forecast as a card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 20.dp, bottom = 5.dp),
        elevation = 8.dp,
        backgroundColor = Color.LightGray
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = getFormattedDate(date), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.subtitle1)
            Divider(color = MaterialTheme.colors.onSurface, modifier = Modifier.padding(vertical = 8.dp))
            LazyRow(
                content = {
                    items(forecastList) { forecastItem ->
                        ForecastCard(forecastItem)
                    }
                }
            )
        }
    }
}

/**
 * A composable function to display individual forecast cards.
 *
 * @param forecastItem The forecast item to display.
 */
@Composable
fun ForecastCard(forecastItem: ForecastItem) {
    val time = getFormattedTime(forecastItem.dt_txt)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colors.primaryVariant)
            .width(120.dp)
            .padding(8.dp)
    ) {
        Text(text = time, style = MaterialTheme.typography.subtitle2, color = Color.White)
        Text(text = "${forecastItem.main?.temp} °C", style = MaterialTheme.typography.body2, color = Color.White)
    }
}