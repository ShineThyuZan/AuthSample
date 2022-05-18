package com.galaxytechno.chat.presentation.ui.auth.signup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.galaxytechno.chat.databinding.SimpleSpinnerItemBinding
import com.galaxytechno.chat.model.dto.SecurityQuestObj


class SampleCustomArraryAdapter
    (context: Context, private val questionList: List<SecurityQuestObj>) :
    ArrayAdapter<SecurityQuestObj>(context, 0, questionList) {

    private lateinit var view: View
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = SimpleSpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val question = questionList[position]
        binding.tvSecurityQuestion.text = question.question

        return binding.root

    }

}