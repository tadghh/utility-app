package com.example.utilityapp.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.example.utilityapp.data.NoteDao
import kotlinx.coroutines.DelicateCoroutinesApi


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
//    val notesList = noteDao.getAllNotes().observeAsState(emptyList())
//
//    // State to track whether the user is creating a new note
//    var isCreatingNote by remember { mutableStateOf(false) }
//    var isCreatingCategory by remember { mutableStateOf(false) }
//    var selectedNote: Note? by remember { mutableStateOf(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTabIndex) {
                0 -> {
                    NotesTab(noteDao = noteDao, categoryDao = categoryDao)
                }

                1 -> {
                    WeatherTab()
                }
            }
        }
        // Bottom Navigation
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
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

                    },
                )
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Add or Delete a Category",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Select a Category",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 32.dp, bottom = 2.dp, end = 32.dp, start = 32.dp)
        )

        // Initial category selection dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 32.dp, end = 32.dp, start = 32.dp)
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

        // Category chosen label
        Text(
            text = "Category Chosen: $selectedCategoryName",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp)
        )

        // Delete button
        Button(
            onClick = {
                selectedCategoryName = ""
                onCategoryDeleted(selectedCategoryId)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 2.dp, end = 16.dp, start = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Text("Delete")
        }

        // Cancel button
        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 30.dp, end = 16.dp, start = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant)
        ) {
            Text("Cancel", color = Color.White)
        }

        // Create new category button
        Button(
            onClick = { isCreatingNewCategory = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Create New Category")
        }

        // New category creation input fields and buttons
        if (isCreatingNewCategory) {
            TextField(
                value = newCategoryName,
                onValueChange = { newCategoryName = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )

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
    }
}