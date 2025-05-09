package com.appmoviles.mercaapp.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appmoviles.mercaapp.ui.components.TextInput
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, navController: NavController? = null) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Scaffold {
        paddingValues ->
        Column(
            modifier.fillMaxSize().padding(paddingValues),
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
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                )

            )
            TextInput(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.8f),
                name = "Contraseña",
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            TextInput(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.8f),
                name = "Confirmar contraseña",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()

            )
            Button(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .width(225.dp),
                onClick =
                    {
                        register(name, email, password, confirmPassword) { success, message ->
                            if (success) {
                                navController?.navigate("login")
                            }
                            else {
                                errorMessage = message
                            }
                        }
                    }
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
                    ),
                    modifier = Modifier.clickable(onClick = {
                        navController?.navigate("login") {
                            popUpTo("register") { inclusive = true } // Para no volver con "Back"
                        }
                    })
                )
            }
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

}

fun register( name: String, email: String, password: String, confirmPassword: String, onResult: (Boolean, String?) -> Unit){
    lateinit var auth: FirebaseAuth
    auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    val user = hashMapOf(
        "nombre" to name,
        "correo" to email
    )

    if (password != confirmPassword) {
        onResult(false, "Las contraseñas no coinciden")
        return
    }

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                db.collection("usuarios")
                    .document(auth.currentUser!!.uid)
                    .set(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added with ID: ${auth.currentUser!!.uid}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                onResult(true, null)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                onResult(false, task.exception?.message)
            }
        }

}