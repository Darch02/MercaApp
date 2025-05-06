package com.example.mercaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mercaapp.ui.components.TachableListItem
import com.example.mercaapp.ui.components.BottomNavigationBar
import com.example.mercaapp.ui.components.TextInput

@Composable
fun ListDetailView(modifier: Modifier = Modifier, navController: NavController){
    val initialTasks = listOf("Comprar pan", "Lavar el coche", "Escribir un correo", "Hacer ejercicio")
    val tasks = remember { mutableStateListOf(*initialTasks.toTypedArray()) }
    val taskStates = remember { mutableStateMapOf<String, Boolean>().apply { initialTasks.forEach { this[it] = false } } }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController!!)

        },
        content = { paddingValues ->
            Column(
                modifier = modifier.padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 24.dp)

            )
            {
                Text(
                    text = "Nombre lista",
                    style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp
                    )
                )
                tasks.forEach { task ->
                    TachableListItem(
                        text = task,
                        isDone = taskStates[task] ?: false,
                        onToggle = { isChecked -> taskStates[task] = isChecked }
                    )
                }
                SmallFloatingActionButton (
                    onClick = { /* Acción al hacer clic */ }){
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(horizontal = 10.dp)) {
                        Icon(Icons.Filled.Add, "Agregar")
                        Text("Agregar", style = MaterialTheme.typography.labelSmall)
                    }

                }
            }
        }
    )
}

@Composable
fun PopUpwNeListItem(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier)
{
    var nombre by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text("Nueva lista") },
        text = {
            Column {
                TextInput(
                    value = nombre,
                    onValueChange = { nombre = it },
                    name = "nombre",
                    modifier = modifier
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                println("Nombre ingresado: $nombre")
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}