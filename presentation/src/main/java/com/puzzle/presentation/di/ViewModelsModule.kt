package com.puzzle.presentation.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.puzzle.auth.graph.login.LoginViewModel
import com.puzzle.auth.graph.signup.SignUpViewModel
import com.puzzle.auth.graph.verification.VerificationViewModel
import com.puzzle.matching.graph.block.BlockViewModel
import com.puzzle.matching.graph.contact.ContactViewModel
import com.puzzle.matching.graph.detail.MatchingDetailViewModel
import com.puzzle.matching.graph.main.MatchingViewModel
import com.puzzle.matching.graph.preview.ProfilePreviewViewModel
import com.puzzle.matching.graph.report.ReportViewModel
import com.puzzle.profile.graph.basic.BasicProfileViewModel
import com.puzzle.profile.graph.main.MainProfileViewModel
import com.puzzle.profile.graph.register.RegisterProfileViewModel
import com.puzzle.profile.graph.valuepick.ValuePickViewModel
import com.puzzle.profile.graph.valuetalk.ValueTalkViewModel
import com.puzzle.setting.graph.main.SettingViewModel
import com.puzzle.setting.graph.withdraw.WithdrawViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchingViewModel::class)
    fun matchingViewModelFactory(factory: MatchingViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(MatchingDetailViewModel::class)
    fun matchingDetailViewModelFactory(factory: MatchingDetailViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun loginViewModelFactory(factory: LoginViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    fun signUpViewModelFactory(factory: SignUpViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(VerificationViewModel::class)
    fun verificationViewModelFactory(factory: VerificationViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(RegisterProfileViewModel::class)
    fun registerProfileViewModelFactory(factory: RegisterProfileViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(MainProfileViewModel::class)
    fun mainProfileViewModelFactory(factory: MainProfileViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    fun settingViewModelFactory(factory: SettingViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(WithdrawViewModel::class)
    fun settingWithdrawViewModelFactory(factory: WithdrawViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ValueTalkViewModel::class)
    fun valueTalkViewModelFactory(factory: ValueTalkViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ValuePickViewModel::class)
    fun valuePickViewModelFactory(factory: ValuePickViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel::class)
    fun reportViewModelFactory(factory: ReportViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(BlockViewModel::class)
    fun blockViewModelFactory(factory: BlockViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(BasicProfileViewModel::class)
    fun basicProfileViewModelFactory(factory: BasicProfileViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    fun contactViewModelFactory(factory: ContactViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ProfilePreviewViewModel::class)
    fun profilePreviewViewModelFactory(factory: ProfilePreviewViewModel.Factory): AssistedViewModelFactory<*, *>
}
