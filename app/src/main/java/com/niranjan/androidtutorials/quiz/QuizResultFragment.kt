package com.niranjan.androidtutorials.quiz

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.FragmentQuizResultBinding

class QuizResultFragment : Fragment(){

    lateinit var binding : FragmentQuizResultBinding

    // Retrieve our arguments
    val args : QuizResultFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_quiz_result,
            container,
            false
        )

        val numberOfQuestions = args.numQuestions
        val numberOfCorrectAnswers = args.numCorrect

        setClickListeners()
        return binding.root
    }

    private fun setClickListeners(){
        binding.nextMatchButton.setOnClickListener {
            // navigate
            findNavController().navigate(R.id.action_quizResultFragment_to_quizFragment)
        }
    }
}