package com.example.mercaapp.ui.screens

import android.content.Context
import android.widget.Toast
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
import com.example.mercaapp.MainActivity
import com.example.mercaapp.ui.components.TextInput
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@Composable
fun LoginScreen(context: Context, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var user : FirebaseUser
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
        Button(modifier = Modifier.padding(vertical = 24.dp).width(225.dp),
            onClick = {  user = Login(context, email, password) }) {
            Text("Ingresar", style = MaterialTheme.typography.labelSmall)
        }
    }
}


fun Login(context: Context, email: String, password: String): Boolean{
    lateinit var auth: FirebaseAuth
    auth = FirebaseAuth.getInstance()
    if (email.isNotEmpty() && password.isNotEmpty()) {
        // Llama al método de Firebase para iniciar sesión con email y contraseña
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as MainActivity) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = auth.currentUser
                    // Redirige a la siguiente actividad o realiza las acciones necesaria
                } else {

                }
            }
    } else {
        Toast.makeText(context, "Por favor, ingresa email y contraseña.", Toast.LENGTH_SHORT).show()
    }
}

