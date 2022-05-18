package com.galaxytechno.chat.presentation.ui.auth.signup.question

import com.galaxytechno.chat.model.dto.SecurityQuestObj

interface QuestCListDelegate {
    fun onQuestCClicked(questData : SecurityQuestObj)
}