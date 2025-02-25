package com.puzzle.piece

import android.app.Application
import android.util.Log
import com.airbnb.mvrx.Mavericks
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PieceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initMavericks()
        initKakao()

        var keyHash = Utility.getKeyHash(this)
        Log.d("test", keyHash.toString())
    }

    private fun initMavericks() = Mavericks.initialize(this)
    private fun initKakao() = KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
}
