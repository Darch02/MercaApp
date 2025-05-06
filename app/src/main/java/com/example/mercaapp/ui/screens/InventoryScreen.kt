package com.example.mercaapp.ui.screens

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
import com.example.mercaapp.ui.components.BottomNavigationBar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ArrowForward

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.navigation.NavController


@Composable
fun InventoryScreen(modifier: Modifier = Modifier, navController: NavController) {
    var alimentosExpanded by remember { mutableStateOf(false) }
    var limpiezaExpanded by remember { mutableStateOf(true) }
    var mascotasExpanded by remember { mutableStateOf(false) }
    var otrosExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
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

            ExpandableCategory(
                title = "Alimentos",
                icon = Icons.Default.ShoppingCart,
                expanded = alimentosExpanded,
                onToggle = { alimentosExpanded = !alimentosExpanded },
                products = listOf("Producto 1", "Producto 2")
            )

            ExpandableCategory(
                title = "Productos de limpieza",
                icon = Icons.Default.Delete,
                expanded = limpiezaExpanded,
                onToggle = { limpiezaExpanded = !limpiezaExpanded },
                products = listOf("Producto", "Producto")
            )

            ExpandableCategory(
                title = "Mascotas",
                icon = Icons.Default.Favorite,
                expanded = mascotasExpanded,
                onToggle = { mascotasExpanded = !mascotasExpanded },
                products = listOf()
            )

            ExpandableCategory(
                title = "Otros",
                icon = Icons.Default.MoreVert,
                expanded = otrosExpanded,
                onToggle = { otrosExpanded = !otrosExpanded },
                products = listOf()
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

            if (showDialog) {
                AddProductDialog(
                    onDismiss = { showDialog = false },
                    onAdd = {
                        // Aquí podrías guardar el producto
                        showDialog = false
                    }
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
    products: List<String>
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
                        IconButton(onClick = { /* Acción eliminar */ }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onAdd: () -> Unit) {
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
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .fillMaxWidth(0.85f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .background(Color.White),
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
                    value = "",
                    onValueChange = {},
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Categoría") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Cantidad") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
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
