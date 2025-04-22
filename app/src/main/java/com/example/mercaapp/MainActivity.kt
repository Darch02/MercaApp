package com.example.mercaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mercaapp.ui.theme.MercaAppTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercaAppTheme(dynamicColor = false) {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    OnboardingScreen()
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido a",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "MercaApp",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Aquí podrás crear tus listas de mercado, tener un inventario y mucho más!",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 24.dp).width(331.dp)
        )
        Button(modifier = Modifier.padding(vertical = 24.dp).width(225.dp), onClick = {}) {
            Text("Continuar", style = MaterialTheme.typography.labelSmall)
        }
        Row () {
            Text(
                text = "¿Ya tienes una cuenta? ",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Inicia sesión",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        TextInput(
            modifier = Modifier.padding(vertical = 15.dp).fillMaxWidth(0.8f), // También puedes limitar el ancho aquí
            name = "Email",
            value = email,
            onValueChange = { email = it }
        )
        TextInput(
            modifier = Modifier.padding(vertical = 15.dp).fillMaxWidth(0.8f),
            name = "Contraseña",
            value = password,
            onValueChange = { password = it }
        )
        Text(
            text = "Olvidé mi contraseña",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Button(modifier = Modifier.padding(vertical = 24.dp).width(225.dp), onClick = {}) {
            Text("Ingresar", style = MaterialTheme.typography.labelSmall)
        }
    }
}


@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    onValueChange: (String) -> Unit
    ) {
        OutlinedTextField(
            modifier = modifier,
            shape = RoundedCornerShape(10.dp),
            value = value,
            onValueChange = onValueChange,
            label = { Text(name, style = MaterialTheme.typography.labelSmall) }
        )
}

@Composable
fun ListsView(modifier: Modifier = Modifier){
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
                    color = MaterialTheme.colorScheme.primary,
                    modifier = modifier.fillMaxWidth().padding(vertical = 24.dp)
                    )
                listCard()
                listCard()
                listCard()
                ExtendedFloatingActionButton(
                    onClick = {  },
                    icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
                    text = { Text(text = "Nueva lista") },
                    modifier = modifier.padding(vertical = 24.dp)
                )
            }
        }
    )
}

@Composable
fun listCard(modifier: Modifier = Modifier){
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 10.dp),
    )
    {
        Text("Lista de mercado", modifier.padding(10.dp), style = MaterialTheme.typography.labelSmall)
        Text("cantidad de productos: 5", modifier.padding(horizontal = 10.dp, vertical = 5.dp), style = MaterialTheme.typography.bodyLarge )
    }
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    NavigationBar(
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO: Navegar a Inicio*/ },
            icon = { Icon(Icons.Filled.Menu, contentDescription = "Listas") },
            label = { Text("Listas") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO: Navegar a Inicio*/ },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inventario") },
            label = { Text("Inventario") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO: Navegar a Perfil*/ },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    MercaAppTheme(dynamicColor = false) {
       OnboardingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MercaAppTheme(dynamicColor = false) {
        LoginScreen()
    }
}
@Preview(showBackground = true)
@Composable
fun listsViewPreview() {
    MercaAppTheme(dynamicColor = false) {
        ListsView()
    }
}