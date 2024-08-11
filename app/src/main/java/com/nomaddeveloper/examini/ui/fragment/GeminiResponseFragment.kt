package com.nomaddeveloper.examini.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.databinding.FragmentGeminiResponseBinding
import com.nomaddeveloper.examini.util.Constant.DEFAULT_POINT
import com.nomaddeveloper.examini.util.Constant.DEFAULT_RESPONSE
import com.nomaddeveloper.examini.util.Constant.KEY_GEMINI_POINT
import com.nomaddeveloper.examini.util.Constant.KEY_GEMINI_RESPONSE

class GeminiResponseFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentGeminiResponseBinding
    private lateinit var geminiResponseContext: Context
    private lateinit var geminiResponse: String
    private lateinit var ratingBarRB: RatingBar
    private lateinit var responseTV: TextView
    private lateinit var backToHomeButton: MaterialButton
    private var geminiPoint: Float = -1f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            geminiResponse = it.getString(KEY_GEMINI_RESPONSE, DEFAULT_RESPONSE)
            geminiPoint = it.getFloat(KEY_GEMINI_POINT, DEFAULT_POINT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeminiResponseBinding.inflate(layoutInflater)
        setupUseful()
        setupUI()
        return binding.root
    }

    private fun setupUseful() {
        geminiResponseContext = requireContext()
    }

    private fun setupUI() {
        binding.apply {
            responseTV = tvGeminiResponseText
            ratingBarRB = rbGeminiResponseRating
            backToHomeButton = mbGeminiResponseBackToHome
        }
        ratingBarRB.rating = geminiPoint
        responseTV.text = geminiResponse
        backToHomeButton.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(geminiResponse: String, point: Float) =
            GeminiResponseFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_GEMINI_RESPONSE, geminiResponse)
                    putFloat(KEY_GEMINI_POINT, point)
                }
            }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                backToHomeButton.id -> {
                    backToHome()
                }

                else -> {
                    //ignored
                }
            }
        }
    }

    private fun backToHome() {
        parentFragmentManager.apply {
            beginTransaction().setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_left
            ).remove(this@GeminiResponseFragment).commit()
            popBackStack()
        }
    }
}