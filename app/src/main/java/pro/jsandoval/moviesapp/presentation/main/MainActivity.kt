package pro.jsandoval.moviesapp.presentation.main

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pro.jsandoval.movieapp.utils.base.BaseActivity
import pro.jsandoval.moviesapp.R
import pro.jsandoval.moviesapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment).navController
    }

    override fun init() {
        initViews()
    }

    private fun initViews() {
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() = with(binding) {
        bottomNavigationView.setupWithNavController(navController)
    }
}