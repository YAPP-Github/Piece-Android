package com.puzzle.profile.graph.register.contract

import androidx.compose.runtime.Composable
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType

sealed class RegisterProfileIntent {
    data class OnNickNameChange(val nickName: String) : RegisterProfileIntent()
    data class OnProfileImageChanged(val imageUri: String) : RegisterProfileIntent()
    data object OnBackClick : RegisterProfileIntent()
    data class OnSaveClick(val registerProfileState: RegisterProfileState) : RegisterProfileIntent()
    data object OnDuplicationCheckClick : RegisterProfileIntent()
    data class OnSelfDescriptionChange(val description: String) : RegisterProfileIntent()
    data class OnBirthdateChange(val birthday: String) : RegisterProfileIntent()
    data class OnHeightChange(val height: String) : RegisterProfileIntent()
    data class OnWeightChange(val weight: String) : RegisterProfileIntent()
    data class OnJobClick(val job: String) : RegisterProfileIntent()
    data class OnRegionClick(val region: String) : RegisterProfileIntent()
    data class OnIsSmokeClick(val isSmoke: Boolean) : RegisterProfileIntent()
    data class OnSnsActivityClick(val isSnsActivity: Boolean) : RegisterProfileIntent()
    data class OnContactSelect(val idx: Int, val contact: Contact) : RegisterProfileIntent()
    data class OnDeleteContactClick(val idx: Int) : RegisterProfileIntent()
    data class OnAddContactClick(val contactType: ContactType) : RegisterProfileIntent()
    data class ShowBottomSheet(val content: @Composable () -> Unit) : RegisterProfileIntent()
    data object HideBottomSheet : RegisterProfileIntent()
    data object OnHomeClick : RegisterProfileIntent()
    data object OnCheckMyProfileClick : RegisterProfileIntent()
}
