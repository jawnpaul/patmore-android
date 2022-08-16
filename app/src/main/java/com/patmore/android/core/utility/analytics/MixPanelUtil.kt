package com.patmore.android.core.utility.analytics

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.patmore.android.BuildConfig
import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MixPanelUtil @Inject constructor(@ApplicationContext context: Context) {

    private val mixPanel = MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_TOKEN)

    fun identifyUser(userId: String) {
        mixPanel.identify(userId)
        mixPanel.people.identify(userId)
    }

    fun logScreen(screen: String?) {
        val eventString = "Screen - $screen"
        mixPanel.track(eventString)
    }

    fun profileUpdate(id: String) {
        mixPanel.identify(id)
        mixPanel.people.identify(id)

        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        mixPanel.registerSuperProperties(jsonObject)
        mixPanel.people.set(jsonObject)
    }

    fun twitterLogin(data: TweetAuthor) {
        val jsonObject = JSONObject()
        jsonObject.put("userId", data.id)
        jsonObject.put("userHandle", data.userName)
        jsonObject.put("userName", data.name)

        mixPanel.track("twitterLogin", jsonObject)
    }
}
