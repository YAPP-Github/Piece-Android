package com.puzzle.profile.graph.register.contract

import androidx.compose.runtime.Composable
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform
import com.puzzle.navigation.NavigationEvent

sealed class RegisterProfileIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : RegisterProfileIntent()
    data class UpdateNickName(val nickName: String) : RegisterProfileIntent()
    data class UpdateDescribeMySelf(val description: String) : RegisterProfileIntent()
    data class UpdateBirthday(val birthday: String) : RegisterProfileIntent()
    data class UpdateHeight(val height: String) : RegisterProfileIntent()
    data class UpdateWeight(val weight: String) : RegisterProfileIntent()
    data class UpdateJob(val job: String) : RegisterProfileIntent()
    data class UpdateRegion(val region: String) : RegisterProfileIntent()
    data class UpdateSmokeStatus(val isSmoke: Boolean) : RegisterProfileIntent()
    data class UpdateSnsActivity(val isSnsActivity: Boolean) : RegisterProfileIntent()
    data class UpdateContact(val idx: Int, val contact: Contact) : RegisterProfileIntent()
    data class DeleteContact(val idx: Int) : RegisterProfileIntent()
    data class AddContact(val snsPlatform: SnsPlatform) : RegisterProfileIntent()
    data class ShowBottomSheet(val content: @Composable () -> Unit) : RegisterProfileIntent()
}
