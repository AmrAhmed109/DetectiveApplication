package com.example.detectiveapplication.feature.home.utils

sealed class Capital(val capitalName: String) {
    object All : Capital("الكل")
    object Cairo : Capital("القاهرة")
    object BeniSuef : Capital("بنى سويف")
    object Alexandria : Capital("اسكندرية")
    object Giza : Capital("جيزة")
    object Qalyubia : Capital("قليوبية")
    object Gharbia : Capital("غربية")
    object Dakahlia : Capital("دقهلية")
    object Asyut : Capital("اسيوط")
    object Fayoum : Capital("فيوم")
    object Sharqia : Capital("شرقية")
    object Ismailia : Capital("اسماعلية")
    object Aswan : Capital("اسوان")
    object Beheira : Capital("البحيرة")
}

val capitalList = listOf(
    Capital.All,
    Capital.Cairo,
    Capital.BeniSuef,
    Capital.Alexandria,
    Capital.Giza,
    Capital.Qalyubia,
    Capital.Gharbia,
    Capital.Dakahlia,
    Capital.Asyut,
    Capital.Fayoum,
    Capital.Sharqia,
    Capital.Ismailia,
    Capital.Aswan,
    Capital.Beheira
)

object CapitalUtils {
    fun getCapital(capitalName: String): Capital {
        return when (capitalName) {
            "الكل" -> Capital.All
            "القاهرة" -> Capital.Cairo
            "بنى سويف" -> Capital.BeniSuef
            "Alexandria" -> Capital.Alexandria
            "Giza" -> Capital.Giza
            "Qalyubia" -> Capital.Qalyubia
            "Gharbia" -> Capital.Gharbia
            "Dakahlia" -> Capital.Dakahlia
            "Asyut" -> Capital.Asyut
            "Fayoum" -> Capital.Fayoum
            "Sharqia" -> Capital.Sharqia
            "Ismailia" -> Capital.Ismailia
            "Aswan" -> Capital.Aswan
            "Beheira" -> Capital.Beheira
            else -> Capital.All
        }
    }
}