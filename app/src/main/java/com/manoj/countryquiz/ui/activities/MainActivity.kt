package com.manoj.countryquiz.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.manoj.countryquiz.R
import com.manoj.countryquiz.databinding.ActivityMainBinding
import com.manoj.countryquiz.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.questionTimeLeft.observe(this) { timeLeft ->
            binding.tvSecZ.text = timeLeft

        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getQuestionnaireProgress()
        mainViewModel.continueQuiz.observe(this) { continueQuiz ->
            if (continueQuiz) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.setGraph(R.navigation.nav_graph_session)
            } else {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.setGraph(R.navigation.nav_graph)
            }
        }
        mainViewModel.isProgress.observe(this) { isProgress ->
            if (isProgress) {
                mainViewModel.checkQuizExpired()
            } else {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.setGraph(R.navigation.nav_graph)
            }
        }
    }
}