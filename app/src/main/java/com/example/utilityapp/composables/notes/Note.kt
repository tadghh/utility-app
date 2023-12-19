package com.example.utilityapp.composables.notes

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.utilityapp.R
import com.example.utilityapp.composables.observeAsState
import com.example.utilityapp.data.Category
import com.example.utilityapp.data.Note


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
	val focusManager = LocalFocusManager.current

	Column(
		Modifier
			.pointerInput(Unit) {
				detectTapGestures(onTap = {
					focusManager.clearFocus()
				})
			}
			.padding(16.dp)
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
		Row(Modifier.padding(5.dp)) {
			Text(
				text = stringResource(R.string.app_name),
				fontSize = 20.sp,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.padding(vertical = 18.dp, horizontal = 2.dp)
					.align(Alignment.CenterVertically)

			)
			ExposedDropdownMenuBox(
				expanded = expandedCatDropdown,
				onExpandedChange = {
					expandedCatDropdown = !expandedCatDropdown
				},
				modifier = Modifier
					.padding(vertical = 23.dp)
					.height(55.dp)
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
				colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant)
			) {
				Text("Cancel", color = Color.White)
			}

			// Delete button (show only if it's an existing note)s
			if (note != null) {
				Button(
					onClick = {
						onDelete(note)
					},
					colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
				) {
					Text("Delete")
				}
			}
		}
	}
}