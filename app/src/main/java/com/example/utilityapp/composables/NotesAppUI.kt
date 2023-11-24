package com.example.utilityapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.utilityapp.data.Category
import com.example.utilityapp.data.CategoryDao
import com.example.utilityapp.data.Note
import com.example.utilityapp.data.NoteDao
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

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
        val observer = androidx.lifecycle.Observer<T> { value ->
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
 * Composable for displaying a single note item.
 *
 * @param note The note to display.
 * @param onItemClick The action to perform when the note is clicked.
 */
@Composable
fun NoteItem(
    note: Note,
    onItemClick: (Note) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = { onItemClick(note) })
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Composable for the main UI of the notes app.
 *
 * @param noteDao Data access object for notes.
 * @param categoryDao Data access object for categories.
 */
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NotesAppUI(
    noteDao: NoteDao,
    categoryDao: CategoryDao,
) {
    var selectedTabIndex by remember { mutableStateOf(0) } // to track the selected tab index

    // Define the tabs for bottom navigation
    val tabs = listOf("Notes", "Weather")

    // Starts off as an empty list till the state is updated later on.
    val notesList = noteDao.getAllNotes().observeAsState(emptyList())

    // State to track whether the user is creating a new note
    var isCreatingNote by remember { mutableStateOf(false) }
    var isCreatingCategory by remember { mutableStateOf(false) }
    var selectedNote: Note? by remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            when (selectedTabIndex) {
                0 -> {
                    NotesTab(noteDao = noteDao, categoryDao = categoryDao)
                }

                1 -> {
                    WeatherTab()
                }
            }
            // Bottom Navigation
            BottomNavigation(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 8.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    BottomNavigationItem(
                        icon = {
                            when (index) {
                                0 -> Icons.Default.Home // "Note" icon for the left tab
                                1 -> Icons.Default.Search // "Weather" icon for the right tab
                            }
                        },
                        label = { Text(text = title) },
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotesTab(
    noteDao: NoteDao,
    categoryDao: CategoryDao,
) {
    // Starts off as an empty list till the state is updated later on.
    val notesList = noteDao.getAllNotes().observeAsState(emptyList())

    // State to track whether the user is creating a new note
    var isCreatingNote by remember { mutableStateOf(false) }
    var isCreatingCategory by remember { mutableStateOf(false) }
    var selectedNote: Note? by remember { mutableStateOf(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.height(705.dp)
        ) {
            // Title
            Text(
                text = "My Notes",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(16.dp)
            )

            // LazyColumn to display notes
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // state updates here
                items(notesList) { note ->
                    NoteItem(note = note)
                    {
                        selectedNote = note
                        isCreatingNote = true
                    }
                }
            }

            // Button to add a new note
            Button(
                onClick = {
                    selectedNote = null // Clear the selected note
                    isCreatingNote = true
                    isCreatingCategory = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Add New Note")
            }

            // Button to add a new note
            Button(
                onClick = {
                    selectedNote = null // Clear the selected note
                    isCreatingNote = false
                    isCreatingCategory = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Add/delete Categories")
            }
        }
        if (isCreatingNote) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
            {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            // Show the note creation screen when isCreatingNote is true
                            NoteCreationScreen(
                                note = selectedNote,
                                onNoteCreated = { newNote ->
                                    val note = Note(
                                        title = newNote.title,
                                        content = newNote.content,
                                        categoryId = newNote.categoryId
                                    )

                                    GlobalScope.launch(Dispatchers.Main) { noteDao.insert(note) }
                                    selectedNote = null
                                    isCreatingNote = false
                                },
                                onNoteEdited = { editNote ->
                                    GlobalScope.launch(Dispatchers.Main) { noteDao.update(editNote) }
                                    selectedNote = null
                                    isCreatingNote = false
                                },
                                onCancel = {
                                    selectedNote = null
                                    isCreatingNote = false
                                },
                                onDelete = { noteToDelete ->
                                    GlobalScope.launch(Dispatchers.Main) {
                                        noteDao.delete(
                                            noteToDelete
                                        )
                                    }
                                    selectedNote = null
                                    isCreatingNote = false
                                },
                                categories = categoryDao.getAllCategories(),
                            )
                        }
                    }
                }
            }
        }
        if (isCreatingCategory) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    CategorySelectionScreen(
                        categories = categoryDao.getAllCategories(),
                        onCategoryCreated = { categoryName ->
                            val category = Category(name = categoryName)
                            GlobalScope.launch(Dispatchers.Main) { categoryDao.insert(category) }
                        },
                        onCategoryDeleted = { categoryId ->
                            if (categoryId != null) {
                                GlobalScope.launch(Dispatchers.IO) { categoryDao.delete(categoryId) }
                            }
                        },
                        onCancel = {
                            isCreatingCategory = false
                            isCreatingNote = false
                        }
                    )
                }
            }
        }
    }
}

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

data class Coord(
    val lon: Double?,
    val lat: Double?
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Main(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val humidity: Int?
)

data class Wind(
    val speed: Double?,
    val deg: Int?,
    val gust: Double?
)

data class Clouds(
    val all: Int?
)

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)

// Define a callback interface
interface WeatherDataCallback {
    fun onSuccess(data: String)
    fun onFailure(error: String)
}

var weatherData: WeatherData = WeatherData(null, null, null, null,
    null, null, null, null, null, null, null, null, null)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherTab() {
    var selectedWeatherInterval by remember { mutableStateOf("Current Weather") }

        Column(
            modifier = Modifier
                .height(705.dp)
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Weather Data",
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

                val weatherDataTypes = listOf("Current Weather", "3-hour Forecast 5 days")

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

                val apiKey = "a66838394baf9c9ddf43532a3e3377c1"
                val baseUrl = "https://api.openweathermap.org/data/2.5/"


                    if(selectedWeatherInterval == "Current Weather")  {
                        val currentWeatherUrl =
                            "$baseUrl/weather?lat=49.895138&lon=-97.138374&appid=$apiKey"
                        fetchWeatherData(currentWeatherUrl, object : WeatherDataCallback {
                            override fun onSuccess(data: String) {
                                // Handle the successful response here
                                weatherData = parseJsonToWeatherData(data)

                                println("Test: ${weatherData.weather}" )
                                //println("Weather data: $data")
                            }
                            override fun onFailure(error: String) {
                                // Handle the failure here
                                println("Error: $error")
                            }
                        })
                    }
                    else {
                        val forecastUrl =
                            "$baseUrl/forecast?lat=49.895138&lon=-97.138374&appid=$apiKey"
                        fetchWeatherData(forecastUrl, object : WeatherDataCallback {
                            override fun onSuccess(data: String) {
                                // Handle the successful response here
                                println("Weather data: $data")
                            }
                            override fun onFailure(error: String) {
                                // Handle the failure here
                                println("Error: $error")
                            }
                        })
                    }
            }
            //if (weatherData.weather != null) {
                DisplayWeather(weatherData)
            //}

            // Display the selected weather interval
            Text(
                text = "Selected Interval: $selectedWeatherInterval",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
}

// Parse JSON data to WeatherData object
fun parseJsonToWeatherData(jsonData: String): WeatherData {
    // Parse JSON string to WeatherData object
    // Example: Use a JSON parsing library like Kotlinx.serialization, Gson, or Moshi
    // For simplicity, here's a direct parsing implementation for the provided JSON structure
    val gson = Gson() // Using Gson for simple JSON parsing
    val weatherData: WeatherData= gson.fromJson(jsonData, WeatherData::class.java)

    return  weatherData
}

@Composable
fun DisplayWeather(weatherData: WeatherData) {
    Column {
        Text(text = "City: ${weatherData.name}")
        Text(text = "Temperature: ${weatherData.main?.temp} K")
        Text(text = "Description: ${weatherData.weather?.firstOrNull()?.description ?: "N/A"}")
        Text(text = "Pressure: ${weatherData.main?.pressure} hPa")
        Text(text = "Humidity: ${weatherData.main?.humidity}%")
        Text(text = "Wind Speed: ${weatherData.wind?.speed} m/s")
        // Display other relevant weather information similarly
    }
}

val client = OkHttpClient()
fun fetchWeatherData(url: String, callback: WeatherDataCallback) {
    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            callback.onFailure("Network error: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val data = response.body()?.string()
                if (data != null) {
                    callback.onSuccess(data)
                } else {
                    callback.onFailure("Empty response body")
                }
            } else {
                callback.onFailure("Unsuccessful response: ${response.code()}")
            }
        }
    })
}

/**
 * Composable for creating or editing a note.
 *
 * @param note The note to be created or edited.
 * @param onNoteCreated Callback when a new note is created.
 * @param onNoteEdited Callback when an existing note is edited.
 * @param onCancel Callback when the user cancels the operation.
 * @param onDelete Callback when the user deletes a note.
 * @param categories LiveData of categories.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteCreationScreen(
    note: Note?, // nullable note parameter in-case we are opening a note instead of creating
    onNoteCreated: (Note) -> Unit,
    onNoteEdited: (Note) -> Unit,
    onCancel: () -> Unit,
    onDelete: (Note) -> Unit,
    categories: LiveData<List<Category>>,
) {
    // Define state variables for user input
    val categoriesList = categories.observeAsState(emptyList())

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var categoryId by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier.height(705.dp)
    ) {
        // Input fields for title, content, category, and reminder
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )

        var expandedCatDropdown by remember { mutableStateOf(false) }
        var selectedCategoryName by remember { mutableStateOf("") }

        // If an existing category ID is provided, find and set the initial state
        if (note?.categoryId != null) {
            val existingCategory = categoriesList.find { it.id == note.categoryId }
            if (existingCategory != null) {
                selectedCategoryName = existingCategory.name
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedCatDropdown,
                onExpandedChange = {
                    expandedCatDropdown = !expandedCatDropdown
                }
            ) {

                TextField(
                    value = selectedCategoryName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expandedCatDropdown,
                    onDismissRequest = { expandedCatDropdown = false }
                ) {
                    categoriesList.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCategoryName = category.name
                                note?.categoryId = category.id
                                categoryId = category.id
                                expandedCatDropdown = false
                            }
                        ) {
                            Text(text = category.name)
                        }
                    }
                }
            }
        }

        // Save, cancel, and delete buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (note == null) {
                        val newNote = Note(
                            id = 0L,
                            title = title,
                            content = content,
                            categoryId = categoryId,
                        )
                        onNoteCreated(newNote)
                    } else {
                        note.title = title
                        note.content = content
                        onNoteEdited(note)
                    }
                },
            ) {
                if (note == null) {
                    Text("Create")
                } else {
                    Text("Save")
                }
            }

            // Cancel button
            Button(
                onClick = onCancel,
            ) {
                Text("Cancel")
            }

            // Delete button (show only if it's an existing note)
            if (note != null) {
                Button(
                    onClick = {
                        onDelete(note)
                    },
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

/**
 * Composable for creating or deleting categories.
 *
 * @param categories LiveData of categories.
 * @param onCategoryCreated Callback when a new category is created.
 * @param onCategoryDeleted Callback when a category is deleted.
 * @param onCancel Callback when the user cancels the operation.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategorySelectionScreen(
    categories: LiveData<List<Category>>,
    onCategoryCreated: (String) -> Unit,
    onCategoryDeleted: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val categoriesList = categories.observeAsState(emptyList())
    var newCategoryName by remember { mutableStateOf("") }
    var isCreatingNewCategory by remember { mutableStateOf(false) }
    var selectedCategoryName by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var expandedCatDropdown by remember { mutableStateOf(false) }

    Box(
        Modifier.height(705.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = "Select Category",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedCatDropdown,
                        onExpandedChange = {
                            expandedCatDropdown = !expandedCatDropdown
                        }
                    ) {

                        TextField(
                            value = selectedCategoryName,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expandedCatDropdown,
                            onDismissRequest = { expandedCatDropdown = false }
                        ) {
                            categoriesList.forEach { category ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedCategoryName = category.name
                                        selectedCategoryId = category.id
                                        expandedCatDropdown = false
                                        isCreatingNewCategory = false
                                    }
                                ) {
                                    Text(text = category.name)
                                }
                            }
                        }
                    }
                }

                // Button to show text field for creating a new category
                Button(
                    onClick = { isCreatingNewCategory = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Create new Category")
                }

                if (isCreatingNewCategory) {
                    // Text field for entering a new category name
                    TextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Save and Cancel buttons for creating a new category
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                if (newCategoryName.isNotBlank()) {
                                    onCategoryCreated(newCategoryName)
                                    newCategoryName = ""
                                }
                            }
                        ) {
                            Text("Add")
                        }

                        Button(
                            onClick = {
                                isCreatingNewCategory = false
                                newCategoryName = ""
                            },
                        ) {
                            Text("Cancel")
                        }
                    }
                }

                selectedCategoryName.let { categoryName ->
                    Text("Category chosen: $categoryName", modifier = Modifier.padding(top = 16.dp))

                    Button(
                        onClick = {
                            selectedCategoryName = ""
                            onCategoryDeleted(selectedCategoryId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            //.padding(top = 16.dp)
                            .padding(16.dp)
                    ) {
                        Text("Delete")
                    }
                }

                Button(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.padding(top = 16.dp)
                        .padding(16.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}