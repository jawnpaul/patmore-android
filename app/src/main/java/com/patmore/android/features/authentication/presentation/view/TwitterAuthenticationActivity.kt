package com.patmore.android.features.authentication.presentation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.patmore.android.BuildConfig
import com.patmore.android.R
import com.patmore.android.core.utility.generateAuthenticationUrl
import com.patmore.android.features.authentication.data.remote.oauth.OAuthScope
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class TwitterAuthenticationActivity : AppCompatActivity() {

    private val callbackUrl = BuildConfig.TWITTER_CALLBACK_URL

    lateinit var oauthToken: String

    private val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val _state = (1..10)
        .map { allowedChars.random() }
        .joinToString("")

    private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '-' + '.' + '_' + '~'

    private val challenge = (1..44)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twitter_authentication)

        val clientId = intent.getStringExtra("clientId")!!
        val scopeList = listOf(OAuthScope.OfflineAccessScope, OAuthScope.TweetReadScope, OAuthScope.UserReadScope, OAuthScope.UserFollowsReadScope)

        /*OAuthScope.OfflineAccessScope,
        OAuthScope.TweetReadScope,
        OAuthScope.TweetWriteScope,
        OAuthScope.TweetModerateWrite,
        OAuthScope.UserBlockReadScope,
        OAuthScope.UserBlockWriteScope,
        OAuthScope.UserFollowsReadScope,
        OAuthScope.UserFollowsWriteScope,
        OAuthScope.UserMuteReadScope,
        OAuthScope.UserMuteWriteScope,
        OAuthScope.UserReadScope,
        OAuthScope.LikeWrite,
        OAuthScope.LikeRead*/

        val url = generateAuthenticationUrl(
            clientId,
            scopeList, callbackUrl, _state, challenge
        )

        val webView: WebView = findViewById(R.id.auth_web_view)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = webViewClient
        webView.loadUrl(url)
    }

    private val webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            if (request!!.url.toString().startsWith(callbackUrl)) {
                val decodedUrl = URLDecoder.decode(request.url.toString(), "UTF-8")
                if (decodedUrl.contains("code=")) {
                    val uri = Uri.parse(decodedUrl)
                    val code = uri.getQueryParameter("code")
                    val state = uri.getQueryParameter("state")

                    // Check to make sure the state generated matches the one returned in the url
                    if (state == _state) {
                        val data = Intent()
                        data.putExtra("code", code)
                        data.putExtra("challenge", challenge)
                        setResult(RESULT_OK, data)
                        finish()
                    } else {
                        // Tokens don't match, do not proceed
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                }
                return true
            }
            return false
        }
    }
}
