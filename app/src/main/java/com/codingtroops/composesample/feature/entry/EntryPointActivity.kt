package com.codingtroops.composesample.feature.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.codingtroops.composesample.feature.category_details.FoodCategoryDetailsScreen
import com.codingtroops.composesample.feature.category_details.FoodCategoryDetailsViewModel
import com.codingtroops.composesample.feature.category_details.FoodCategoryViewModelFactory
import com.codingtroops.composesample.feature.entry.NavigationKeys.Arg.FOOD_CATEGORY_ID
import com.codingtroops.composesample.feature.categories.FoodCategoriesContract
import com.codingtroops.composesample.feature.categories.FoodCategoriesScreen
import com.codingtroops.composesample.feature.categories.FoodCategoriesViewModel
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme


// Single Activity per app
class EntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                FoodApp()
            }
        }
    }

}

@Composable
private fun FoodApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
        composable(route = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
            val viewModel: FoodCategoriesViewModel = viewModel()
            val state = viewModel.viewState.collectAsState().value
            FoodCategoriesScreen(
                state = state,
                effectFlow = viewModel.effect,
                onEventSent = { event -> viewModel.setEvent(event) },
                onNavigationRequested = { navigationEffect ->
                    if (navigationEffect is FoodCategoriesContract.Effect.Navigation.ToCategoryDetails) {
                        navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/${navigationEffect.categoryName}")
                    }
                })
        }
        composable(
            route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.FOOD_CATEGORY_ID) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val categoryId =
                backStackEntry.arguments!!.getString(NavigationKeys.Arg.FOOD_CATEGORY_ID)!!
            val viewModel: FoodCategoryDetailsViewModel =
                viewModel(factory = FoodCategoryViewModelFactory(categoryId))
            val state = viewModel.viewState.collectAsState().value
            FoodCategoryDetailsScreen(state)
        }
    }
}

object NavigationKeys {

    object Arg {
        const val FOOD_CATEGORY_ID = "foodCategoryName"
    }

    object Route {
        const val FOOD_CATEGORIES_LIST = "food_categories_list"
        const val FOOD_CATEGORY_DETAILS = "$FOOD_CATEGORIES_LIST/{$FOOD_CATEGORY_ID}"
    }

}