package com.example.mercaapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mercaapp.ui.theme.MercaAppTheme
import androidx.compose.ui.text.style.TextAlign
import com.example.mercaapp.ui.screens.*

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
    OnboardingScreen(modifier)
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
        Row {
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
fun ListViewPreview() {
    MercaAppTheme(dynamicColor = false) {
        ListsView()
    }
}
@Preview(showBackground = true)
@Composable
fun ListsDetailViewPreview() {
    MercaAppTheme(dynamicColor = false) {
        ListDetailView()
    }
}