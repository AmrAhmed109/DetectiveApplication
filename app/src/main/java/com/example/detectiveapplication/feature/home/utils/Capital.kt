package com.example.detectiveapplication.feature.home.utils

sealed class Capital(val capitalName: String) {
    object All : Capital("الكل")
    object Cairo : Capital("القاهرة")
    object BeniSuef : Capital("بنى سويف")
    object Alexandria : Capital("Alexandria")
    object Giza : Capital("Giza")
    object Qalyubia : Capital("Qalyubia")
    object Gharbia : Capital("Gharbia")
    object Dakahlia : Capital("Dakahlia")
    object Asyut : Capital("Asyut")
    object Fayoum : Capital("Fayoum")
    object Sharqia : Capital("Sharqia")
    object Ismailia : Capital("Ismailia")
    object Aswan : Capital("Aswan")
    object Beheira : Capital("Beheira")
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