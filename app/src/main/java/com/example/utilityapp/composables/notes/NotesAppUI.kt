package com.example.utilityapp.composables.notes

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.utilityapp.R
import com.example.utilityapp.composables.observeAsState
import com.example.utilityapp.composables.weather.WeatherTab
import com.example.utilityapp.data.Category
import com.example.utilityapp.data.CategoryDao
import com.example.utilityapp.data.NoteDao


/**
 * Composable for the main UI of the notes app.
 *
 * @param noteDao Data access object for notes.
 * @param categoryDao Data access object for categories.
 */
@Composable
fun NotesAppUI(
	noteDao: NoteDao,
	categoryDao: CategoryDao,
) {
	var selectedTabIndex: Int by remember { mutableIntStateOf(0) } // to track the selected tab index

	// Define the tabs for bottom navigation
	val tabs = listOf(stringResource(R.string.notes), stringResource(R.string.weather))

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
	var isEditingCategory by remember { mutableStateOf(false) }

	//Used to keep track of input
	val focusManager = LocalFocusManager.current
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxHeight()
	) {
		//Allows you to click off a text input, this is placed on the parent element otherwise we cant click "anywhere"
		Column(Modifier.pointerInput(Unit) {
			detectTapGestures(onTap = {
				focusManager.clearFocus()
			})
		}) {
			if (isCreatingNewCategory) {
				Text(
					text = stringResource(R.string.create) + " " + stringResource(
						R.string
							.category
					),
					style = MaterialTheme.typography.h5,
					modifier = Modifier
						.padding(top = 32.dp, bottom = 10.dp)
						.align(Alignment.CenterHorizontally)
				)
				// Text field for entering a new category name
				TextField(

					value = newCategoryName,
					onValueChange = { newCategoryName = it },
					label = {
						Text(
							stringResource(R.string.category_name)
						)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp)
						.align(Alignment.CenterHorizontally)
				)

				// Save and Cancel buttons for creating a new category

				Button(modifier = Modifier
					.fillMaxWidth()
					.padding(top = 32.dp, bottom = 16.dp, start = 100.dp, end = 100.dp),

					onClick = {
						if (newCategoryName.isNotBlank()) {
							onCategoryCreated(newCategoryName)
							newCategoryName = ""
						}
					}
				) {
					Text(stringResource(R.string.add))
				}

				Button(modifier = Modifier
					.fillMaxWidth()
					.padding(top = 0.dp, bottom = 16.dp, start = 100.dp, end = 100.dp),
					colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
					onClick = {
						isCreatingNewCategory = false
						isEditingCategory = false
						newCategoryName = ""
					}
				) {
					Text(stringResource(R.string.cancel), color = Color.White)
				}
			}

			if (!isEditingCategory) {
				Text(
					text = stringResource(R.string.category_chosen),
					style = MaterialTheme.typography.h5,
					modifier = Modifier
						.padding(top = 32.dp, bottom = 10.dp)
						.align(Alignment.CenterHorizontally)
				)

				ExposedDropdownMenuBox(
					expanded = expandedCatDropdown,
					onExpandedChange = {
						expandedCatDropdown = !expandedCatDropdown
					},
					modifier = Modifier
						.padding(horizontal = 1.dp)
						.align(Alignment.CenterHorizontally)

				) {

					TextField(
						value = selectedCategoryName,
						onValueChange = {},
						readOnly = true,

						trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) }
					)

					DropdownMenu(
						expanded = expandedCatDropdown,
						onDismissRequest = { expandedCatDropdown = false },

						) {
						categoriesList.forEach { category ->
							DropdownMenuItem(
								onClick = {
									selectedCategoryName = category.name
									selectedCategoryId = category.id
									expandedCatDropdown = false
									isCreatingNewCategory = false
								},
							) {
								Text(
									text = category.name,
									modifier = Modifier
										.padding(horizontal = 1.dp)
										.fillMaxWidth()
								)
							}
						}
					}
				}

				if (selectedCategoryName == "") {
					Text(
						stringResource(R.string.no_category_selected),
						style = MaterialTheme.typography.h6,
						modifier = Modifier
							.padding(top = 20.dp)
							.align(alignment = Alignment.CenterHorizontally)
					)
				} else {
					Text(
						stringResource(R.string.category_chosen) + " " + selectedCategoryName,
						style = MaterialTheme.typography.h6,
						modifier = Modifier
							.padding(top = 20.dp)
							.align(alignment = Alignment.CenterHorizontally)
					)
				}
				Button(
					onClick = {
						selectedCategoryName = ""
						onCategoryDeleted(selectedCategoryId)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 32.dp, bottom = 16.dp, start = 100.dp, end = 100.dp),
					colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
				) {
					Text(stringResource(R.string.delete))
				}
			}

			Spacer(modifier = Modifier.weight(1f))
			// Button to show text field for creating a new category
			Button(
				onClick = { isCreatingNewCategory = true; isEditingCategory = true },
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)

			) {
				Text(stringResource(R.string.create_category))
			}
			Button(
				onClick = onCancel,
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant)
			) {
				Text(stringResource(R.string.view_notes), color = Color.White)
			}
		}

	}
}