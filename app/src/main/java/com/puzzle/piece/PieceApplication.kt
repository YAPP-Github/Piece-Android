package com.puzzle.piece

import android.app.Application
import com.airbnb.mvrx.Mavericks
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PieceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initMavericks()
        initKakao()
    }

    private fun initMavericks() = Mavericks.initialize(this)
    private fun initKakao() = KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
}