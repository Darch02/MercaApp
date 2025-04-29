package com.example.mercaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mercaapp.ui.components.TextInput

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        TextInput(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.8f),
            name = "Nombre",
            value = name,
            onValueChange = { name = it }
        )
        TextInput(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.8f),
            name = "Email",
            value = email,
            onValueChange = { email = it }
        )
        TextInput(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.8f),
            name = "Contraseña",
            value = password,
            onValueChange = { password = it }
        )
        TextInput(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.8f),
            name = "Confirmar contraseña",
            value = confirmPassword,
            onValueChange = { confirmPassword = it }
        )
        Button(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .width(225.dp),
            onClick = { /* Acción de registro */ }
        ) {
            Text("Registrarme", style = MaterialTheme.typography.labelSmall)
        }
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "¿Ya tienes una cuenta? ")
            Text(
                text = "Inicia sesión",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                // Aquí podrías agregar navegación al LoginScreen
            )
        }
    }
}
