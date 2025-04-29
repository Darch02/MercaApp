package com.example.mercaapp.ui.screens

import android.view.textclassifier.TextLinks.TextLink
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier ,
    navController: NavController? = null
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
        Button(modifier = Modifier.padding(vertical = 24.dp).width(225.dp),
            onClick = {
                navController?.navigate("SignIn")
            })
        {
            Text("Continuar", style = MaterialTheme.typography.labelSmall)
        }
        Row {
            Text(
                text = "¿Ya tienes una cuenta? ",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Inicia sesión",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable(onClick = {
                    navController?.navigate("login")
                })
            )
        }
    }
}