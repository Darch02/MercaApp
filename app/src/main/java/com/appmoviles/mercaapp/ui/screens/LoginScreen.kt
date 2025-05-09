package com.appmoviles.mercaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.appmoviles.mercaapp.ui.components.TextInput
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoginScreen( modifier: Modifier = Modifier, navController: NavController? = null) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Scaffold {
        paddingValues ->
        Column(
            modifier.fillMaxSize().padding(paddingValues),
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
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                )
            )
            TextInput(
                modifier = Modifier.padding(vertical = 15.dp).fillMaxWidth(0.8f),
                name = "Contraseña",
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Text(
                text = "Olvidé mi contraseña",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Button(
                modifier = Modifier.padding(vertical = 24.dp).width(225.dp),
                onClick = {
                    Login( email, password) { success, message -> // Llama a Login con el callback
                        if (success) {
                            navController?.navigate("home") {
                                popUpTo("onboarding") { inclusive = true } // Para no volver con "Back"
                            }
                        } else {
                            errorMessage = message // Muestra el error
                        }
                    }
                }
            )
            {
                Text("Ingresar", style = MaterialTheme.typography.labelSmall)
            }
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

}


fun Login( email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    lateinit var auth: FirebaseAuth
    auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, null) // Éxito, no hay error
            } else {
                onResult(false, task.exception?.message) // Fallo, pasa el mensaje de error
            }
        }
}

