package com.appmoviles.mercaapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import com.appmoviles.mercaapp.ui.components.BottomNavigationBar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ArrowForward

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


@Composable
fun InventoryScreen(modifier: Modifier = Modifier, navController: NavController) {
    var alimentosExpanded by remember { mutableStateOf(false) }
    var limpiezaExpanded by remember { mutableStateOf(true) }
    var mascotasExpanded by remember { mutableStateOf(false) }
    var otrosExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // 🔄 Recarga de productos al agregar
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var reloadTrigger by remember { mutableStateOf(0) }
    val productos = remember { mutableStateListOf<Map<String, Any>>() }

    // 🚀 Cargar productos del inventario
    LaunchedEffect(userId, reloadTrigger) {
        if (userId != null) {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("usuarios").document(userId).collection("inventario")
                .get().await()

            productos.clear()
            productos.addAll(snapshot.documents.mapNotNull { it.data })
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        val nombreProductoState = remember { mutableStateOf("") }
        val categoriaState = remember { mutableStateOf("") }
        val cantidadState = remember { mutableStateOf("") }
        val unidadesState = remember { mutableStateOf("") }
        val scrollState = rememberScrollState()


        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .verticalScroll(scrollState), // ← Este es el cambio clave
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Inventario",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp)
                )

                val alimentos = productos.filter { it["categoria"] == "Alimentos" }.map { it["nombre"].toString() }
                ExpandableCategory(
                    title = "Alimentos",
                    icon = Icons.Default.ShoppingCart,
                    expanded = alimentosExpanded,
                    onToggle = { alimentosExpanded = !alimentosExpanded },
                    products = alimentos,
                    onDelete = { nombre ->
                        if (userId != null) {
                            deleteInventoryItem(userId, nombre) { success, error ->
                                if (success) reloadTrigger++ else println("Error: $error")
                            }
                        }
                    }
                )

                val limpieza = productos
                    .filter { it["categoria"] == "Productos de Aseo" }
                    .map { it["nombre"].toString() }

                ExpandableCategory(
                    title = "Productos de Aseo",
                    icon = Icons.Default.Delete,
                    expanded = limpiezaExpanded,
                    onToggle = { limpiezaExpanded = !limpiezaExpanded },
                    products = limpieza,
                    onDelete = { nombre ->
                        if (userId != null) {
                            deleteInventoryItem(userId, nombre) { success, error ->
                                if (success) reloadTrigger++ else println("Error: $error")
                            }
                        }
                    }
                )


                val mascotas = productos
                    .filter { it["categoria"] == "Mascotas" }
                    .map { it["nombre"].toString() }

                ExpandableCategory(
                    title = "Mascotas",
                    icon = Icons.Default.Favorite,
                    expanded = mascotasExpanded,
                    onToggle = { mascotasExpanded = !mascotasExpanded },
                    products = mascotas,
                    onDelete = { nombre ->
                        if (userId != null) {
                            deleteInventoryItem(userId, nombre) { success, error ->
                                if (success) reloadTrigger++ else println("Error: $error")
                            }
                        }
                    }
                )


                val otros = productos
                    .filter { it["categoria"] == "Otros" }
                    .map { it["nombre"].toString() }

                ExpandableCategory(
                    title = "Otros",
                    icon = Icons.Default.MoreVert,
                    expanded = otrosExpanded,
                    onToggle = { otrosExpanded = !otrosExpanded },
                    products = otros,
                    onDelete = { nombre ->
                        if (userId != null) {
                            deleteInventoryItem(userId, nombre) { success, error ->
                                if (success) reloadTrigger++ else println("Error: $error")
                            }
                        }
                    }
                )


                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                ) {
                    Text("+ Agregar productos al inventario")
                }

            }
            if (showDialog) {
                AddProductDialog(
                    onDismiss = { showDialog = false },
                    onAdd = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@AddProductDialog

                        val newItem = mapOf(
                            "nombre" to nombreProductoState.value,
                            "categoria" to categoriaState.value,
                            "cantidad" to cantidadState.value,
                            "unidades" to unidadesState.value
                        )

                        saveInventoryItem(userId, newItem) { success, error ->
                            if (success) {
                                showDialog = false
                                reloadTrigger++ // Esto fuerza recarga de productos en pantalla
                            } else {
                                println("Error guardando producto: $error")
                            }
                        }
                    }
                    ,
                    nombreProductoState,
                    categoriaState,
                    cantidadState,
                    unidadesState
                )
            }
        }
    }
}

@Composable
fun ExpandableCategory(
    title: String,
    icon: ImageVector,
    expanded: Boolean,
    onToggle: () -> Unit,
    products: List<String>,
    onDelete: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.ArrowForward,
                contentDescription = "Toggle"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            products.forEach { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = product)
                        IconButton(onClick = { onDelete(product) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
    nombreProductoState: MutableState<String>,
    categoriaState: MutableState<String>,
    cantidadState: MutableState<String>,
    unidadesState: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Alimentos", "Productos de Aseo", "Mascotas", "Otros")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onDismiss()
            }
    )
    {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .fillMaxWidth(0.85f),
            shape = RoundedCornerShape(20.dp)
        )
        {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inventario",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = nombreProductoState.value,
                    onValueChange = { nombreProductoState.value = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box {
                    OutlinedTextField(
                        value = categoriaState.value,
                        onValueChange = { /* No permitir edición directa */ },
                        label = { Text("Categoría") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown, contentDescription = null,
                                Modifier.clickable { expanded = !expanded })
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true // Evita la edición directa
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    categoriaState.value = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = cantidadState.value,
                        onValueChange = { cantidadState.value = it },
                        label = { Text("Cantidad") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unidadesState.value,
                        onValueChange = { unidadesState.value = it },
                        label = { Text("Unidades") },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onAdd()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar producto")
                }
            }
        }
    }
}

fun saveInventoryItem(
    userId: String,
    item: Map<String, Any>,
    onComplete: (Boolean, String?) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("usuarios").document(userId).collection("inventario")

    ref.add(item)
        .addOnSuccessListener { onComplete(true, null) }
        .addOnFailureListener { e -> onComplete(false, e.message) }
}

fun deleteInventoryItem(
    userId: String,
    nombre: String,
    onComplete: (Boolean, String?) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("usuarios").document(userId).collection("inventario")

    ref.whereEqualTo("nombre", nombre).get()
        .addOnSuccessListener { querySnapshot ->
            val batch = db.batch()
            for (document in querySnapshot.documents) {
                batch.delete(document.reference)
            }
            batch.commit()
                .addOnSuccessListener { onComplete(true, null) }
                .addOnFailureListener { e -> onComplete(false, e.message) }
        }
        .addOnFailureListener { e ->
            onComplete(false, e.message)
        }
}


