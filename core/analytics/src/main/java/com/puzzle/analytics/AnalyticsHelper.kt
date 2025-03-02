package com.puzzle.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.puzzle.analytics.AnalyticsEvent.PropertiesKeys.BUTTON_NAME
import com.puzzle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.puzzle.analytics.AnalyticsEvent.Types.BUTTON_CLICK
import com.puzzle.analytics.AnalyticsEvent.Types.SCREEN_VIEW

abstract class AnalyticsHelper {
    abstract fun logEvent(event: AnalyticsEvent)
    abstract fun setUserId(userId: String?)
}

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    NoOpAnalyticsHelper()
}

@Composable
fun TrackScreenViewEvent(
    key: Any?,
    screenName: String?,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = LaunchedEffect(key) {
    if (screenName != null) {
        analyticsHelper.logEvent(
            AnalyticsEvent(
                type = SCREEN_VIEW,
                properties = mutableMapOf(
                    SCREEN_NAME to screenName,
                ),
            ),
        )
    }
}

@Composable
fun TrackClickEvent(
    key: Any?,
    screenName: String,
    buttonName: String,
    properties: MutableMap<String, Any?>? = null,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = LaunchedEffect(key) {
    val eventProperties = mutableMapOf<String, Any?>(
        SCREEN_NAME to screenName,
        BUTTON_NAME to buttonName,
    )

    properties?.let { eventProperties.putAll(it) }

    analyticsHelper.logEvent(
        AnalyticsEvent(
            type = BUTTON_CLICK,
            properties = eventProperties
        )
    )
}
