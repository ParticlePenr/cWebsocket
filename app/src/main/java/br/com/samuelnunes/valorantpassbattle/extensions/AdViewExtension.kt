package br.com.samuelnunes.valorantpassbattle.extensions.bindingAdapters

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

fun AdView.load() {
    val adRequest: AdRequest = AdRequest.Builder().build()
    loadAd(adRequest)
}


