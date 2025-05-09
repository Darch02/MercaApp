package com.appmoviles.mercaapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(name, style = MaterialTheme.typography.labelSmall) },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

@Composable
fun ListCard(modifier: Modifier = Modifier, nombreLista: String, cantidadItems: Int, onClick: () -> Unit){
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 10.dp),
        onClick = onClick
    )
    {
        Text(nombreLista, modifier.padding(10.dp), style = MaterialTheme.typography.labelSmall)
        Text("cantidad de productos: $cantidadItems", modifier.padding(horizontal = 10.dp, vertical = 5.dp), style = MaterialTheme.typography.bodyLarge )
    }
}

@Composable
fun TachableListItem(text: String, isDone: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isDone) }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Checkbox(
            checked = isDone,
            onCheckedChange = onToggle
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                fontSize = 20.sp
            ),
            color = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f) // Para que el texto ocupe la mayor parte del espacio
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Menu, contentDescription = "Listas") },
            label = { Text("Listas") }
        )
        NavigationBarItem(
            selected = currentRoute == "inventario",
            onClick = {
                if (currentRoute != "inventario") {
                    navController.navigate("inventario") {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inventario") },
            label = { Text("Inventario") }
        )
        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = {
                if (currentRoute != "perfil") {
                    navController.navigate("perfil") {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}

@Composable
fun PopUpConfirmDelete(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onListDelete: () -> Unit,
    nombreLista: String)
{

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text("eliminar lista") },
        text = {
            Text("Estas seguro que quieres eliminar la lista $nombreLista?")
        },
        confirmButton = {
            Button(onClick = {
                var uid : String
                onListDelete()
            })
            {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
