package com.galaxytechno.chat.presentation.ui.auth.signup.question

import com.galaxytechno.chat.model.dto.SecurityQuestObj

interface QuestAListDelegate {
    fun onQuestAClicked(questData : SecurityQuestObj)

}