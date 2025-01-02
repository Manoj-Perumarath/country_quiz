package com.manoj.countryquiz.ui.screen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.manoj.countryquiz.R
import com.manoj.countryquiz.databinding.FragmentQuestionBinding
import com.manoj.countryquiz.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class QuestionFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.saveQuestionnaireProgress(true)
        val questions = mainViewModel.items.value
        mainViewModel.currentIndex.observe(viewLifecycleOwner) { index ->
//            Toast.makeText(context, "index is $index", Toast.LENGTH_SHORT).show()
            val resId = this.resources.getIdentifier(
                "ic_" + mainViewModel.currentIndex.value?.let {
                    questions?.questions?.get(it)?.country_code?.lowercase(
                        Locale.ENGLISH
                    ) ?: "ic_launcher_foreground"
                }, "drawable", this.context?.packageName
            )
            binding.ivFlag.setImageResource(resId)

            binding.btnOptOne.btnOpt.apply {
                text =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[0].country_name
                tag =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[0].id.toString()

            }
            binding.btnOptTwo.btnOpt.apply {
                text =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[1].country_name
                tag =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[1].id.toString()

            }
            binding.btnOptThree.btnOpt.apply {
                text =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[2].country_name
                tag =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[2].id.toString()

            }
            binding.btnOptFour.btnOpt.apply {
                text =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[3].country_name
                tag =
                    questions?.questions?.get(mainViewModel.currentIndex.value!!)!!.countries[3].id.toString()

            }
        }
        mainViewModel.startQuestionCounter()
        binding.clickHandler = View.OnClickListener {
            if (mainViewModel.selectedAnswer.value!!.isNotEmpty()) {
                resetLabels(mainViewModel.selectedAnswer.value!!)
            }
            mainViewModel.setSelectedAnswer(it.tag.toString())
            it.setBackgroundResource(R.drawable.bg_option_selected)
        }

        mainViewModel.questionTimeLeft.observe(viewLifecycleOwner) { timeLeft ->
            if (timeLeft == "00" || timeLeft == "00:00") {
                if (mainViewModel.currentIndex.value!! < 14) {
                    mainViewModel.startAnswerValidatorCounter()
                } else {
                    mainViewModel.saveQuestionnaireProgress(false)
                    findNavController().navigate(
                        R.id.gameOverFragment
                    )
                }
            }
        }

        mainViewModel.answerTimeLeft.observe(viewLifecycleOwner) { timeLeft ->
//            Toast.makeText(context, "validator $timeLeft", Toast.LENGTH_SHORT).show()
            if (mainViewModel.currentIndex.value!! < 14) {
                mainViewModel.calculateScore(
                    (mainViewModel.selectedAnswer.value!! == questions!!.questions[mainViewModel.currentIndex.value!!].answer_id.toString())
                )
                showRightAnswer(questions!!.questions[mainViewModel.currentIndex.value!!].answer_id.toString())
            }
            if (timeLeft == "00") {
                if (mainViewModel.currentIndex.value!! < 14) {
                    if (mainViewModel.selectedAnswer.value!!.isNotEmpty()) {
                        resetLabels(mainViewModel.selectedAnswer.value!!)
                    }
                    resetLabels(questions!!.questions[mainViewModel.currentIndex.value!!].answer_id.toString())
                    mainViewModel.incrementQuestionIndex()
                    mainViewModel.startQuestionCounter()
                }

//                findNavController().navigate(R.id.questionFragment)
            }
        }
    }

    private fun resetLabels(tag: String) {
        when (tag) {
            binding.btnOptOne.btnOpt.tag -> {
                binding.btnOptOne.btnOpt.setBackgroundResource(R.drawable.bg_options_default)
                binding.btnOptOne.tvAnswerStatus.apply {
                    text = ""
                }
            }

            binding.btnOptTwo.btnOpt.tag -> {
                binding.btnOptTwo.btnOpt.setBackgroundResource(R.drawable.bg_options_default)
                binding.btnOptTwo.tvAnswerStatus.apply {
                    text = ""
                }
            }

            binding.btnOptThree.btnOpt.tag -> {
                binding.btnOptThree.btnOpt.setBackgroundResource(R.drawable.bg_options_default)
                binding.btnOptThree.tvAnswerStatus.apply {
                    text = ""
                }
            }

            binding.btnOptFour.btnOpt.tag -> {
                binding.btnOptFour.btnOpt.setBackgroundResource(R.drawable.bg_options_default)
                binding.btnOptFour.tvAnswerStatus.apply {
                    text = ""
                }
            }
        }
    }

    private fun showRightAnswer(tag: String) {
        if (mainViewModel.selectedAnswer.value != tag) {
            if (mainViewModel.selectedAnswer.value!!.isNotEmpty()) {
                markAnswerLabel(
                    mainViewModel.selectedAnswer.value!!, false
                )
            }
            markAnswerLabel(tag, true)
        } else {
            markAnswerLabel(tag, true)
        }
    }

    private fun markAnswerLabel(tag: String, isRight: Boolean) {
        when (tag) {
            binding.btnOptOne.btnOpt.tag -> {
                binding.btnOptOne.btnOpt.setBackgroundResource(if (isRight) R.drawable.bg_right_answer else R.drawable.bg_wrong_answer)
                binding.btnOptOne.tvAnswerStatus.apply {
                    text = getString(if (isRight) R.string.correct else R.string.wrong)
                    setTextColor(if (isRight) Color.GREEN else Color.RED)
                }
            }

            binding.btnOptTwo.btnOpt.tag -> {
                binding.btnOptTwo.btnOpt.setBackgroundResource(if (isRight) R.drawable.bg_right_answer else R.drawable.bg_wrong_answer)
                binding.btnOptTwo.tvAnswerStatus.apply {
                    text = getString(if (isRight) R.string.correct else R.string.wrong)
                    setTextColor(if (isRight) Color.GREEN else Color.RED)
                }
            }

            binding.btnOptThree.btnOpt.tag -> {
                binding.btnOptThree.btnOpt.setBackgroundResource(if (isRight) R.drawable.bg_right_answer else R.drawable.bg_wrong_answer)
                binding.btnOptThree.tvAnswerStatus.apply {
                    text = getString(if (isRight) R.string.correct else R.string.wrong)
                    setTextColor(if (isRight) Color.GREEN else Color.RED)
                }
            }

            binding.btnOptFour.btnOpt.tag -> {
                binding.btnOptFour.btnOpt.setBackgroundResource(if (isRight) R.drawable.bg_right_answer else R.drawable.bg_wrong_answer)
                binding.btnOptFour.tvAnswerStatus.apply {
                    text = getString(if (isRight) R.string.correct else R.string.wrong)
                    setTextColor(if (isRight) Color.GREEN else Color.RED)
                }
            }
        }
    }
}