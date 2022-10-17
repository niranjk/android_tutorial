package com.niranjan.androidtutorials.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.FragmentQuizBinding

class QuizFragment : Fragment(){

    val quizList = mutableListOf<QuizUiModel>(
        QuizUiModel(
            "\"Prince Caspian\" is the second in WHICH series of children\'s books by CS Lewis?",
            listOf(
                "Harry Potter", "The Secret Seven", "A Series Of Unfortunate Events", "The Chronicles Of Narnia"
            )
        ),
        QuizUiModel(
            "Apart from being a type of meat, what else is Devon?",
            listOf(
                "A Knife\'s edge", "A breed of dog", "A item of clothing", "An English country"
            )
        ),
        QuizUiModel(
            "What does the term \'somniloquy\' refer to?",
            listOf(
                "A part of a theatrical play", "A language device", "Sleep talking", "Any poems about anatomy"
            )
        ),
        QuizUiModel(
            "Charlie Watts has been the drummer of which legendary rock band?",
            listOf(
                "The Rolling Stones", "Green Day", "AC/DC", "U2"
            )
        ),
        QuizUiModel(
            "Which word can go before TOWN or after SHOW?",
            listOf(
                "TOY", "GROUND", "DOWN", "GIRL"
            )
        ),
    )

    val answersList = listOf(
        "The chronicles Of Narnia",
        "An English country",
        "Sleep talking",
        "The Rolling Stones",
        "DOWN"
    )

    lateinit var currentQuiz : QuizUiModel
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var numQuiz = Math.min((quizList.size +1)/2, 3)
    lateinit var binding : FragmentQuizBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )
        // set the first question
        randomizeQuestions()
        // bind the frament to the layout
        binding.game = this
        setClickListeners()
        return binding.root
    }

    private fun setClickListeners(){
        binding.submitButton.setOnClickListener {
                view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.

                if (answers[answerIndex] == answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuiz) {
                        currentQuiz = quizList[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate
                        val actions = QuizFragmentDirections.actionQuizFragmentToQuizResultFragment(
                            numQuiz,
                            questionIndex
                        )

                        view.findNavController().navigate(actions)
                    }
                }
            }
        }
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        quizList.shuffle()
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuiz = quizList[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuiz.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_quiz_question, questionIndex + 1, numQuiz)
    }
}