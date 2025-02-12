package com.puzzle.profile.graph.basic.contract

import androidx.compose.runtime.Composable
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType

sealed class BasicProfileIntent {
    data object OnBackClick : BasicProfileIntent()
    data object SaveBasicProfile : BasicProfileIntent()
    data class UpdateNickName(val nickName: String) : BasicProfileIntent()
    data object CheckNickNameDuplication : BasicProfileIntent()
    data class UpdateProfileImage(val imageUrl: String) : BasicProfileIntent()
    data class UpdateDescribeMySelf(val description: String) : BasicProfileIntent()
    data class UpdateBirthday(val birthday: String) : BasicProfileIntent()
    data class UpdateHeight(val height: String) : BasicProfileIntent()
    data class UpdateWeight(val weight: String) : BasicProfileIntent()
    data class UpdateJob(val job: String) : BasicProfileIntent()
    data class UpdateRegion(val region: String) : BasicProfileIntent()
    data class UpdateSmokeStatus(val isSmoke: Boolean) : BasicProfileIntent()
    data class UpdateSnsActivity(val isSnsActivity: Boolean) : BasicProfileIntent()
    data class UpdateContact(val idx: Int, val contact: Contact) : BasicProfileIntent()
    data class DeleteContact(val idx: Int) : BasicProfileIntent()
    data class AddContact(val contactType: ContactType) : BasicProfileIntent()
    data class ShowBottomSheet(val content: @Composable () -> Unit) : BasicProfileIntent()
    data object HideBottomSheet : BasicProfileIntent()
}
