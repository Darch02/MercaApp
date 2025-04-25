package com.example.mercaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.sp
import com.example.mercaapp.ui.components.ListCard
import com.example.mercaapp.ui.components.BottomNavigationBar
import com.example.mercaapp.ui.components.TextInput
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ListsView(modifier: Modifier = Modifier){
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        },
        content = { paddingValues ->
            Column(modifier = modifier.padding(paddingValues).padding(horizontal = 20.dp, vertical = 24.dp)) {
                Text(
                    text = "Listas de mercado",
                    style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp
                    ),
                    modifier = modifier.fillMaxWidth().padding(vertical = 24.dp)
                )
                ListCard()
                ListCard()
                ListCard()
                ExtendedFloatingActionButton(
                    onClick = { showDialog = true },
                    icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
                    text = { Text(text = "Nueva lista" , style = MaterialTheme.typography.labelSmall) },
                    modifier = modifier.padding(vertical = 24.dp)
                )
            }
            if (showDialog) {
                PopUpNewList(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false }
                )
            }
        }
    )
}

@Composable
fun PopUpNewList(
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
