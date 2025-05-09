package com.appmoviles.mercaapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.appmoviles.mercaapp.ui.theme.MercaAppTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appmoviles.mercaapp.ui.screens.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercaAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "onboarding") {
                    composable("onboarding") {
                        OnboardingScreen(navController = navController)
                    }
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("home") {
                        ListsView(
                            navController = navController,
                            user = getCurrentUser()
                        )
                    }
                    composable("inventario") {
                        InventoryScreen(navController = navController)
                    }
                    composable("perfil") {
                        ProfileScreen(navController = navController)
                    }
                    composable("SignIn") {
                        RegisterScreen(navController = navController)
                    }
                    composable(
                        route = "listdetail/{listId}/{userId}/{listName}",
                        arguments = listOf(
                            navArgument("listId") { type = NavType.StringType },
                            navArgument("userId") { type = NavType.StringType },
                            navArgument("listName") { type = NavType.StringType }
                        )
                    )
                    { backStackEntry ->
                        val listId = backStackEntry.arguments?.getString("listId") ?: ""
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val listName = backStackEntry.arguments?.getString("listName") ?: ""

                        ListDetailView(navController = navController, listId = listId, userId = userId, listName = listName)

                    }
                }

            }
        }
    }

}

fun getCurrentUser(): FirebaseUser? {
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser
}



/*
 */
@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    MercaAppTheme(dynamicColor = false) {
       OnboardingScreen()
    }
}
/*
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
        //ListDetailView()
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(){
    MercaAppTheme(dynamicColor = false) {
        RegisterScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview(){
    MercaAppTheme(dynamicColor = false) {
        ForgotPasswordScreen()
    }
}
/*


 */

@Preview(showBackground = true)
@Composable
fun ProfilePreview(){
    MercaAppTheme(dynamicColor = false) {
        ProfileScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun InventoryPreview(){
    MercaAppTheme(dynamicColor = false) {
        InventoryScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PopUpPreview() {
    MercaAppTheme(dynamicColor = false) {
//        AddProductDialog(
//            onDismiss = {},
//            onAdd = {}
//        )
    }
}
 */
