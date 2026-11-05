package com.example.aa2_sensors

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.aa2_sensors.ui.screens.LevelSelectorScreen
import com.example.aa2_sensors.ui.screens.GameScreen
import com.example.aa2_sensors.ui.screens.MenuScreen
import com.example.aa2_sensors.ui.theme.WithButtonSizes
import kotlinx.serialization.Serializable


const val LEVEL_COUNT = 6

@Serializable
sealed interface Screen {
    @Serializable
    data object Menu : Screen

    @Serializable
    data object LevelSelector : Screen

    @Serializable
    data class InGame(val levelId: Int) : Screen
}

@Composable
fun App() {
    val navController = rememberNavController()


    val backToMenu: () -> Unit = { navController.popBackStack<Screen.Menu>(inclusive = false) }

    WithButtonSizes {
        NavHost(
            navController,
            startDestination = Screen.Menu,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            composable<Screen.Menu> {
                MenuScreen(
                    onPlay = { navController.launchLevel(1) },
                    onShowLevels = {
                        navController.navigate(Screen.LevelSelector) { launchSingleTop = true }
                    },
                )
            }

            composable<Screen.LevelSelector> {
                LevelSelectorScreen(
                    onLevelClick = { id -> navController.launchLevel(id) },
                    onBack = backToMenu,
                )
            }

            composable<Screen.InGame> { entry ->
                val levelId = entry.toRoute<Screen.InGame>().levelId

                GameScreen(
                    levelName = "level $levelId",
                    onExit = backToMenu,
                    onNextLevel = if (levelId < LEVEL_COUNT) {
                        { navController.launchLevel(levelId + 1) }
                    } else null
                )
            }
        }
    }
}


private fun NavController.launchLevel(id: Int) {
    navigate(Screen.InGame(id)) {
        popUpTo<Screen.InGame> { inclusive = true }
        launchSingleTop = true
    }
}
