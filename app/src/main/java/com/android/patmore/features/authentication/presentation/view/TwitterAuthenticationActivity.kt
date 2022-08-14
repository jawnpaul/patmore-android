package com.android.patmore.features.authentication.presentation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.android.patmore.BuildConfig
import com.android.patmore.R
import com.tycz.tweedle.lib.authentication.oauth.OAuthScope
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
        val scopeList = listOf(OAuthScope.OfflineAccessScope, OAuthScope.TweetReadScope, OAuthScope.UserReadScope)

        oauthToken = intent.getStringExtra("oauthToken")!!
        val url = "https://api.twitter.com/oauth/authorize?oauth_token=$oauthToken&force_login=true"

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

        /*val url = Authentication2.generateAuthenticationUrl(
            clientId,
            scopeList, callbackUrl, _state, challenge
        )*/

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
                if (decodedUrl.contains("oauth_token=")) {
                    val uri = Uri.parse(decodedUrl)
                    val authToken = uri.getQueryParameter("oauth_token")
                    val authVerifier = uri.getQueryParameter("oauth_verifier")

                    // Check to make sure oauth tokens match
                    if (oauthToken == authToken) {
                        val data = Intent()
                        data.putExtra("oauth_token", authToken)
                        data.putExtra("oauth_verifier", authVerifier)
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
