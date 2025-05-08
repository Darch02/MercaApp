package com.example.mercaapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mercaapp.ui.components.TachableListItem
import com.example.mercaapp.ui.components.BottomNavigationBar
import com.example.mercaapp.ui.components.PopUpConfirmDelete
import com.example.mercaapp.ui.components.TextInput
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import com.example.mercaapp.ui.screens.AddProductDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue

@Composable

fun ListDetailView(modifier: Modifier = Modifier, navController: NavController? = null, userId: String, listId: String, listName: String){

    val tasks = remember { mutableStateListOf<Map<String, Any>>() }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Cargar los items (tareas) al iniciar o cuando userId o listId cambian
    LaunchedEffect(userId, listId) {
        val items = getListItems(userId, listId)
        tasks.addAll(items)
    }

    // Callback para recargar la lista de items (tareas)
    val refreshListItems = {
        scope.launch {
            val items = getListItems(userId, listId)
            tasks.clear()
            tasks.addAll(items)
        }
    }

    val updateItemState = { itemName: String, isChecked: Boolean ->
        scope.launch {
            updateListItemState(userId, listId, itemName, isChecked)
            // Recargar la lista para reflejar los cambios
            val updatedItems = getListItems(userId, listId)
            tasks.clear()
            tasks.addAll(updatedItems)
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
            Box(
                modifier = modifier
                    .fillMaxSize() // O ajusta la altura según necesites
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                contentAlignment = Alignment.BottomEnd // Alinea el FAB a la esquina inferior derecha inicialmente
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth() // O ajusta el ancho según necesites
                ) {
                    Text(
                        text = listName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 40.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (tasks.isNotEmpty()) {
                        tasks.forEach { task ->
                            TachableListItem(
                                text = task["nombre"].toString() + " " + task["cantidad"].toString() + " " + task["unidades"].toString(),
                                isDone = task["estado"] as? Boolean ?: false,
                                onToggle = { isChecked ->
                                    // Actualizar el estado local inmediatamente para la UI
                                    val index = tasks.indexOf(task)
                                    val updatedTask = task.toMutableMap()
                                    updatedTask["estado"] = isChecked
                                    tasks[index] = updatedTask.toMap()
                                    // Guardar el estado en la base de datos
                                    updateItemState(task["nombre"].toString(), isChecked)
                                }
                            )
                        }
                    } else {
                        Text("No hay items en esta lista.")
                    }

                    var mensaje by remember { mutableStateOf<String?>(null) }

                    Button(
                        onClick = {
                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                val productosTachados = tasks.filter { (it["estado"] as? Boolean) == true }
                                if (productosTachados.isEmpty()) {
                                    mensaje = "No tienes productos seleccionados"
                                } else {
                                    for (producto in productosTachados) {
                                        val item = mapOf(
                                            "nombre" to producto["nombre"].toString(),
                                            "categoria" to producto["categoria"].toString(),
                                            "cantidad" to producto["cantidad"].toString(),
                                            "unidades" to producto["unidades"].toString()
                                        )
                                        saveInventoryItem(user.uid, item) { success, error ->
                                            if (!success) {
                                                println("Error al enviar producto '${item["nombre"]}' al inventario: $error")
                                            }
                                        }
                                    }
                                    mensaje = "Productos enviados correctamente"
                                }
                            }
                        },
                        modifier = modifier.padding(vertical = 20.dp)
                    ) {
                        Text("enviar productos seleccionados al inventario")
                    }

                    mensaje?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier.padding(top = 8.dp)
                        )
                    }


                    Spacer(modifier = Modifier.weight(1f)) // Empuja el FAB hacia abajo
                }
                SmallFloatingActionButton(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart) // Asegura que el FAB esté en la esquina inferior derecha
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(horizontal = 10.dp)
                    ) {
                        Icon(Icons.Filled.Add, "Agregar")
                        Text("Agregar", style = MaterialTheme.typography.labelSmall)
                    }
                }
                SmallFloatingActionButton(
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd),
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(horizontal = 10.dp)
                    ) {
                        Icon(Icons.Filled.Delete, "Agregar")
                        Text("eliminar lista", style = MaterialTheme.typography.labelSmall)
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
            if(showDeleteDialog) {
                PopUpConfirmDelete(
                    showDialog = showDeleteDialog,
                    onDismiss = { showDeleteDialog = false },
                    onListDelete = {
                        deleteList(userId, listId) { success, errorMessage ->
                            if (success) {
                                println("Lista eliminada correctamente.")
                                navController?.navigate("home")
                                // Aquí podrías realizar alguna acción adicional después de la eliminación
                            } else {
                                println("Error al eliminar la lista: $errorMessage")
                                // Aquí podrías manejar el error, mostrar un mensaje al usuario, etc.
                            }
                        }
                    },
                    nombreLista = listName
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
        "categoria" to itemCategory,
        "estado" to false
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

suspend fun updateListItemState(userId: String, listId: String, itemName: String, isChecked: Boolean) {
    val db = FirebaseFirestore.getInstance()
    val userDocumentRef = db.collection("usuarios").document(userId)
    val listDocumentRef = userDocumentRef.collection("lists").document(listId)

    try {
        val documentSnapshot = listDocumentRef.get().await()
        val currentItems = documentSnapshot?.get("items") as? List<Map<String, Any>> ?: emptyList()
        val updatedItems = currentItems.map { item ->
            if (item["nombre"] == itemName) {
                item.toMutableMap().apply { this["estado"] = isChecked }.toMap()
            } else {
                item
            }
        }
        listDocumentRef.update("items", updatedItems).await()
        println("Estado del item '$itemName' actualizado a $isChecked.")
    } catch (e: Exception) {
        println("Error al actualizar el estado del item '$itemName': $e")
    }
}

fun deleteList(userId: String, listId: String, onComplete: (Boolean, String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userDocumentRef = db.collection("usuarios").document(userId)
    val listDocumentRef = userDocumentRef.collection("lists").document(listId)

    listDocumentRef.delete()
        .addOnSuccessListener {
            println("Lista con ID '$listId' eliminada exitosamente del usuario '$userId'.")
            onComplete(true, null)
        }
        .addOnFailureListener { e ->
            println("Error al eliminar la lista '$listId' del usuario '$userId': $e")
            onComplete(false, e.message)
        }
}