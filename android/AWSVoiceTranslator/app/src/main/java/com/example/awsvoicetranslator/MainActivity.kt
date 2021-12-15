/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.awsvoicetranslator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101
    private var myRequest: PermissionRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setWebView();
    }

    fun setWebView(){
        val myWebView = findViewById<WebView>(R.id.webview)

        myWebView.settings.javaScriptEnabled = true

        myWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        myWebView.webViewClient = WebViewClient()

        myWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        myWebView.webViewClient = WebViewClient()
        myWebView.webChromeClient = object : WebChromeClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionRequest(request: PermissionRequest) {
                myRequest = request
                for (permission in request.resources) {
                    when (permission) {
                        "android.webkit.resource.AUDIO_CAPTURE" -> {
                            askForPermission(
                                request.origin.toString(),
                                Manifest.permission.RECORD_AUDIO,
                                MY_PERMISSIONS_REQUEST_RECORD_AUDIO
                            )
                        }
                    }
                }
            }
        }
            val url: String = getString(R.string.cloudfront_url)

            myWebView.loadUrl(url)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun askForPermission(origin: String, permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    permission
                )
            ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(permission),
                    requestCode
                )
            }
        } else {
            myRequest!!.grant(myRequest!!.resources)
        }
    }
}
