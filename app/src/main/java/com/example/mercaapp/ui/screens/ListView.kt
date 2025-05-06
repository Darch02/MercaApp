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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@Composable
fun ListsView( modifier: Modifier = Modifier, navController: NavController? = null, user: FirebaseUser? = null){
    var showDialog by remember { mutableStateOf(false) }
    val listsState = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(user?.uid) {
        listsState.value = getLists(user?.uid.toString())
    }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController!!)

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
                for (list in listsState.value) {
                    ListCard(
                        nombreLista = list["nombre"].toString(),
                        cantidadItems = list["cantidadItems"].toString().toInt(),
                        onClick =  {
                            navController?.navigate("listdetail")
                        }
                    )
                }
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
                    onDismiss = { showDialog = false},
                    userId = user?.uid,
                    onListCreated = {
                        scope.launch {
                            listsState.value = getLists(user?.uid.toString())
                        }
                        showDialog = false
                    }
                )
            }
        }
    )
}

@Composable
fun PopUpNewList(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    userId: String? = null,
    onListCreated: () -> Unit)
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
                var uid : String
                if (userId != null) {
                    addListToUser(userId, nombre, 0)
                    onListCreated()
                } else {
                    onDismiss()
                }
            })
            {
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

fun addListToUser(userId: String, nombre: String, cantidadItems: Int) {

    val db = FirebaseFirestore.getInstance()
    val userDocumentRef = db.collection("usuarios").document(userId)
    val listsCollectionRef = userDocumentRef.collection("lists")

    val newList = hashMapOf(
        "nombre" to nombre,
        "cantidadItems" to cantidadItems,
    )

    listsCollectionRef.add(newList)
        .addOnSuccessListener { documentReference ->
            println("lista agregada con ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            println("Error al agregar la orden: $e")
        }
}

suspend fun getLists(userId: String): List<Map<String, Any>> {
    val db = FirebaseFirestore.getInstance()
    val userDocumentRef = db.collection("usuarios").document(userId)
    val listsCollectionRef = userDocumentRef.collection("lists")

    return try {
        val querySnapshot = listsCollectionRef.get().await()
        querySnapshot.documents.map { it.data ?: emptyMap() }
    } catch (e: Exception) {
        println("Error al obtener listas: $e")
        emptyList()
    }
}
