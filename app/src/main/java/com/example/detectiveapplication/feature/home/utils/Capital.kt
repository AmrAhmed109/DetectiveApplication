package com.example.detectiveapplication.feature.home.utils

sealed class Capital(val capitalName: String) {
    object All : Capital("الكل")
    object Cairo : Capital("القاهرة")
    object BeniSuef : Capital("بني سويف")
    object Giza : Capital("الجيزة")
    object Qalyubia : Capital("القليوبية")
    object Alexandria : Capital("الإسكندرية")
    object Beheira : Capital("البحيرة")
    object Matruh : Capital("مطروح")
    object Damietta : Capital("دمياط")
    object Dakahlia : Capital("الدقهلية")
    object KafrEl_Sheikh : Capital("كفر الشيخ")
    object Gharbia : Capital("الغربية")
    object Sharqia : Capital("الشرقية")
    object PortSaid : Capital("بورسعيد")
    object Ismailia : Capital("الإسماعيلية")
    object Suez : Capital("السويس")
    object Fayoum : Capital("الفيوم")
    object Minya : Capital("المنيا")
    object Asyut : Capital("أسيوط")
    object New_Valley : Capital("الوادي الجديد")
    object Sohag : Capital("سوهاج")
    object Qena : Capital("قنا")
    object Luxor : Capital("الأقصر")
    object Aswan : Capital("أسوان")
}

val capitalList = listOf(
    Capital.All,
    Capital.Cairo,
    Capital.BeniSuef,
    Capital.Giza,
    Capital.Qalyubia,
    Capital.Alexandria,
    Capital.Beheira,
    Capital.Matruh,
    Capital.Damietta,
    Capital.Dakahlia,
    Capital.KafrEl_Sheikh,
    Capital.Gharbia,
    Capital.Sharqia,
    Capital.PortSaid,
    Capital.Ismailia,
    Capital.Suez,
    Capital.Fayoum,
    Capital.Minya,
    Capital.Asyut,
    Capital.New_Valley,
    Capital.Sohag,
    Capital.Luxor,
    Capital.Aswan
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