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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import com.example.mercaapp.ui.screens.AddProductDialog
import com.google.firebase.firestore.FieldValue

@Composable

fun ListDetailView(modifier: Modifier = Modifier, navController: NavController? = null, userId: String, listId: String){

    val tasks = remember { mutableStateListOf<Map<String, Any>>() }
    val taskStates = remember { mutableStateMapOf<String, Boolean>() }
    var showDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Cargar los items (tareas) al iniciar o cuando userId o listId cambian
    LaunchedEffect(userId, listId) {
        val items = getListItems(userId, listId)
        tasks.addAll(items)
        taskStates.apply {
            items.forEach { item -> this[item["nombre"].toString()] = false } // Inicializa el estado como no hecho
        }
    }

    // Callback para recargar la lista de items (tareas)
    val refreshListItems = {
        scope.launch {
            val items = getListItems(userId, listId)
            tasks.clear()
            tasks.addAll(items)
            taskStates.clear()
            taskStates.apply {
                items.forEach { item -> this[item["nombre"].toString()] = false }
            }
        }
    }

    val nombreProductoState = remember { mutableStateOf("") }
    val categoriaState = remember { mutableStateOf("") }
    val cantidadState = remember { mutableStateOf("") }
    val unidadesState = remember { mutableStateOf("") }
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
                if (tasks.isNotEmpty()) {
                    tasks.forEach { task ->
                        TachableListItem(
                            text = task["nombre"].toString(),
                            isDone = taskStates[task["nombre"].toString()] ?: false,
                            onToggle = { isChecked -> taskStates[task["nombre"].toString()] = isChecked }
                        )
                    }
                } else {
                    Text("No hay tareas en esta lista.")
                }
                SmallFloatingActionButton (
                    onClick = {
                        showDialog = true
                    }){
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(horizontal = 10.dp)) {
                        Icon(Icons.Filled.Add, "Agregar")
                        Text("Agregar", style = MaterialTheme.typography.labelSmall)
                    }

                }

            }
            if (showDialog) {
                AddProductDialog(
                    onDismiss = { showDialog = false },
                    onAdd = {
                        addNewItemToList(userId, listId, nombreProductoState.value, cantidadState.value, unidadesState.value, categoriaState.value)
                        showDialog = false
                        refreshListItems()
                    },
                    nombreProductoState,
                    categoriaState,
                    cantidadState,
                    unidadesState
                )
            }
        }
    )
}


suspend fun getListItems(userId: String, listId: String) : List<Map<String, Any>>{
    val db = FirebaseFirestore.getInstance()
    val userDocumentRef = db.collection("usuarios").document(userId)
    val listDocumentRef = userDocumentRef.collection("lists").document(listId)
    val list = mutableListOf<Map<String, Any>>()
    return try {
        val documentSnapshot = listDocumentRef.get().await()
        val items = documentSnapshot?.get("items") as? List<Map<String, Any>> ?: emptyList()
        items
    }
    catch (e: Exception) {
        println("Error al obtener listas: $e")
        emptyList()
    }
}

fun saveNewListItem(userId: String, listId: String, newItem: Map<String, Any>, onComplete: (Boolean, String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userDocumentRef = db.collection("usuarios").document(userId)
    val listDocumentRef = userDocumentRef.collection("lists").document(listId)

    val updates = hashMapOf<String, Any>(
        "items" to FieldValue.arrayUnion(newItem),
        "cantidadItems" to FieldValue.increment(1) // Incrementa el campo cantidadItems en 1
    )

    listDocumentRef.update(updates)
        .addOnSuccessListener {
            println("Item agregado exitosamente a la lista '$listId'.")
            onComplete(true, null)
        }
        .addOnFailureListener { e ->
            println("Error al agregar el item a la lista '$listId': $e")
            onComplete(false, e.message)
        }
}
fun addNewItemToList(userId: String, listId: String, itemName: String, itemQuantity: String, itemUnits: String, itemCategory: String) {
    val newItem = hashMapOf<String, Any>(
        "nombre" to itemName,
        "cantidad" to itemQuantity,
        "unidades" to itemUnits,
        "categoria" to itemCategory
    )

    saveNewListItem(userId, listId, newItem) { success, errorMessage ->
        if (success) {
            // El item se guardó correctamente
            println("Nuevo item '$itemName' guardado en la lista.")
        } else {
            // Hubo un error al guardar el item
            println("Error al guardar el item: $errorMessage")
        }
    }
}