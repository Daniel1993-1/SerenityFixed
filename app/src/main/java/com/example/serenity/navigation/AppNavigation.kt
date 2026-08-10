package com.example.serenity.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.serenity.ui.home.HomeScreen
import com.example.serenity.ui.login.LoginScreen
import com.example.serenity.ui.profile.ProfileScreen
import com.example.serenity.ui.progress.ProgressScreen
import com.example.serenity.ui.questionnaire.GAD7Screen
import com.example.serenity.ui.questionnaire.PHQ9Screen
import com.example.serenity.ui.questionnaire.PSS10Screen
import com.example.serenity.ui.register.RegisterScreen
import com.example.serenity.ui.resources.ResourcesScreen
import com.example.serenity.ui.theme.*

sealed class BottomNavItem(
    val route: String, val label: String,
    val iconSelected: ImageVector, val iconUnselected: ImageVector
) {
    object Home      : BottomNavItem("home",      "Inicio",   Icons.Filled.Home,     Icons.Outlined.Home)
    object Progress  : BottomNavItem("progress",  "Progreso", Icons.Filled.Star,     Icons.Filled.Star)
    object Resources : BottomNavItem("resources", "Recursos", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Profile   : BottomNavItem("profile",   "Perfil",   Icons.Filled.Person,   Icons.Outlined.Person)
}

val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.Progress, BottomNavItem.Resources, BottomNavItem.Profile)

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val routesWithoutNav = listOf("login", "register", "gad7", "pss10", "phq9")
    val showBottomNav = currentDestination?.route !in routesWithoutNav

    Scaffold(
        modifier = modifier,
        containerColor = HomeBgStart,
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(containerColor = HomeBgStart, tonalElevation = 0.dp) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick  = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon  = { Icon(if (selected) item.iconSelected else item.iconUnselected, contentDescription = item.label) },
                            label = { Text(item.label, fontSize = 11.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, fontFamily = RobotoFamily) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = AuroraStart,
                                selectedTextColor   = AuroraStart,
                                unselectedIconColor = TextWhite30,
                                unselectedTextColor = TextWhite30,
                                indicatorColor      = AuroraStart.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "login", modifier = Modifier.padding(innerPadding)) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                    onGoToRegister = { navController.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                    onGoToLogin       = { navController.popBackStack() }
                )
            }
            composable("home") {
                HomeScreen(
                    onGoToGAD7  = { navController.navigate("gad7") },
                    onGoToPSS10 = { navController.navigate("pss10") },
                    onGoToPHQ9  = { navController.navigate("phq9") }
                )
            }
            composable("progress")  { ProgressScreen() }
            composable("resources") { ResourcesScreen() }
            composable("profile") {
                ProfileScreen(onLogout = { navController.navigate("login") { popUpTo("home") { inclusive = true } } })
            }
            composable("gad7")  { GAD7Screen(onFinished  = { navController.popBackStack() }) }
            composable("pss10") { PSS10Screen(onFinished = { navController.popBackStack() }) }
            composable("phq9")  { PHQ9Screen(onFinished  = { navController.popBackStack() }) }
        }
    }
}