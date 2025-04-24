package com.example.mercaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import com.example.mercaapp.ui.components.TextInput

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier.fillMaxSize(),
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



