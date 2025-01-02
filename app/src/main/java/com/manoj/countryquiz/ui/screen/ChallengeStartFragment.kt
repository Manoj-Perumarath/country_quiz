package com.manoj.countryquiz.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.manoj.countryquiz.R
import com.manoj.countryquiz.databinding.FragmentChallengeStartBinding
import com.manoj.countryquiz.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChallengeStartFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentChallengeStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_start, container, false)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.startCountDown()
        mainViewModel.timeLeft.observe(viewLifecycleOwner) { timeLeft ->
            if (timeLeft.equals("00")) {
                mainViewModel.saveQuestionTime(arguments?.getLong("questionTime") ?: 20000L)
                findNavController().navigate(
                    R.id.challengestart_to_question
                )
            }
        }
    }
}