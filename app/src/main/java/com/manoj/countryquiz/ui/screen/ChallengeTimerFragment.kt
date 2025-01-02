package com.manoj.countryquiz.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.manoj.countryquiz.R
import com.manoj.countryquiz.databinding.FragmentChallengeTimerBinding
import com.manoj.countryquiz.utils.showSecondsPicker


class ChallengeTimerFragment : Fragment() {

    private lateinit var binding: FragmentChallengeTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_timer, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.showSecondsPicker({ min, sec ->

            binding.tvMinO.text = min.substring(0, 1)
            binding.tvMinZ.text = min.substring(1)
            binding.tvSecO.text = sec.substring(0, 1)
            binding.tvSecZ.text = sec.substring(1)

            binding.btnSave.setOnClickListener {
                val totalTime = (min.toInt() * 60 + sec.toInt()) * 1000.toLong()
                val action = ChallengeTimerFragmentDirections.challengeTimerToStart(totalTime)
                findNavController().navigate(
                    action,
                    NavOptions.Builder()
                        .setPopUpTo(
                            R.id.challengeTimerFragment,
                            true
                        )
                        .build()
                )
            }
        })
    }
}