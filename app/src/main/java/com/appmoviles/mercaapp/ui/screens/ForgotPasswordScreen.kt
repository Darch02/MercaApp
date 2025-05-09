package com.appmoviles.mercaapp.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appmoviles.mercaapp.ui.components.TextInput

@Composable
fun ForgotPasswordScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var messageSent by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.titleLarge.copy(
                lineHeight = 50.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            textAlign = TextAlign.Center
        )


        Text(
            text = "Ingresa el Email asociado a la cuenta",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextInput(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.8f),
            name = "Nombre",
            value = email,
            onValueChange = { email = it }
        )
        if (messageSent) {
            Text(
                text = "¡Listo! La contraseña fue enviada a tu Email.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Button(
            onClick = {
                // Simula el envío del correo
                messageSent = true
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .width(200.dp)
        ) {
            Text("Volver", style = MaterialTheme.typography.labelSmall)
        }
    }
}
