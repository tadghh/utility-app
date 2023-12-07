package com.example.utilityapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coopt2_fughetabout_it_inc.composables.NoteCreationScreen
import com.example.utilityapp.data.Category
import com.example.utilityapp.data.CategoryDao
import com.example.utilityapp.data.Note
import com.example.utilityapp.data.NoteDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Composable that represents the tab for displaying and managing notes.
 *
 * @param noteDao The Data Access Object for managing notes.
 * @param categoryDao The Data Access Object for managing categories.
 */
@OptIn(DelicateCoroutinesApi::class)
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
                    .padding(top = 16.dp, bottom = 3.dp, end = 16.dp, start = 16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant)
            ) {
                Text(text = "Add New Note", color = Color.White)
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
                    .padding(top = 3.dp, bottom = 16.dp, end = 16.dp, start = 16.dp)

            ) {
                Text(
                    text = "Add/Delete Categories",
                    color = Color.White
                )
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