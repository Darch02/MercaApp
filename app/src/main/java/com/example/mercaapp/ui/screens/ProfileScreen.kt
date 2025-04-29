package com.example.mercaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mercaapp.ui.components.BottomNavigationBar
import com.example.mercaapp.ui.components.TextInput

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("juanaymary@gmail.com") }
    var password by remember { mutableStateOf("********") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 72.dp), // deja espacio para la barra
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hola Juana y Maryangela!",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
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
                onValueChange = { password = it },
                //isPassword = true
            )

            Text(
                text = "Cambiar contraseña",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { /* Acción cambiar contraseña */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Eliminar cuenta",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* Acción eliminar cuenta */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Acción cerrar sesión */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }

        // Barra inferior fija en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomNavigationBar()
        }
    }
}
