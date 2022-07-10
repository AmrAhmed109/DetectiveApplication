package com.example.detectiveapplication.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class Constants {
    companion object {
//        const val BASE_URL = "https://krombo.000webhostapp.com/api/"
//        const val BASE_URL = "https://kidtrack.herokuapp.com/api/"
        const val BASE_URL = "http://abotallal.tech/api/"
        const val IMAGE = "image"
        const val TIME_OUT = 5000.toLong()
        var TOKEN: String = ""

        val LOREM: String = "هذا النص هو مثال لنص يمكن أن يستبدل في نفس المساحة، لقد تم توليد هذا النص من مولد النص العربى، حيث يمكنك أن تولد مثل هذا النص أو العديد من النصوص الأخرى إضافة إلى زيادة عدد الحروف التى يولدها التطبيق. "


        fun String.requestBodyConvert():RequestBody{
            val body :RequestBody = RequestBody.create(MediaType.parse("text/plain"),this)
            return body
        }
        @SuppressLint("Recycle")
        fun imageBody(mContext: Context, uri: Uri, key: String? = null): MultipartBody.Part {
            val p: String
            val cursor = mContext.contentResolver.query(uri, null, null, null, null)

            p = if (cursor == null) {
                uri.path.toString()
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                cursor.getString(idx)
            }
            val file = File(p)
//            compressImage(file)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData(
                key!!,
                file.absolutePath,
                requestFile
            )
        }

        private fun compressImage(file: File) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            val width = options.outWidth
            val height = options.outHeight
            val inSampleSize =
                if (width > height) {
                    if (width > 1024) {
                        width / 1024
                    } else {
                        1
                    }
                } else {
                    if (height > 1024) {
                        height / 1024
                    } else {
                        1
                    }
                }
            options.inJustDecodeBounds = false
            options.inSampleSize = inSampleSize
            val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        }

        @SuppressLint("Recycle")
        fun anotherImageBody(mContext: Context, uri: Uri, key: String? = null): MultipartBody.Part {
            val p: String
            val cursor = mContext.contentResolver.query(uri, null, null, null, null)

            p = if (cursor == null) {
                uri.path.toString()
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                cursor.getString(idx)
            }
            val file = File(p)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData(
                key!!,
                file.absolutePath,
                requestFile
            )
        }



        fun listOfCites(): ArrayList<City> {
            val city = arrayListOf<City>()
            city.add(City("القاهرة", listOf("الزيتون", "الساحل", " سنورس", "الشرابية", "الزاوية الحمراء", "حدائق الأميرية", "حدائق القبة", "روض الفرج", "شبرا", "السلام أول", "السلام ثان", "المرج", "المرج", "مصر الجديدة", "مدينة نصر", "مدينة نصر", "المقطم", "الخليفة", "الخليفة", "السيدة زينب", "طره", "حلوان", "حلوان",)))
            city.add(City("بني سويف", listOf("بني سويف", "بني سويف الجديدة", "الواسطى", "ناصر", "إهناسيا", "ببا", "سمسطا","الفَشْن")))
            city.add(City("الجيزة", listOf("البَدْرْشِين", "الصَّف", "أطْفِيح", "العَيَّاط", "الباويطي", "منشأة القناطر", "أَوْسِيم", "أبو النُمْرُس", "كِرْداسَة", "الحَوامْدِيّة", "الشيخ زايد", "السادس من أكتوبر",)))
            city.add(City("القليوبية", listOf("البَدْرْشِين", "بَنْها", "قَلْيوب", "شُبْرا الخيمة", "القناطر الخيرية", "الخْانْكَة", "كفر شُكر", "طُوخ", "قَها", "العبور", "الخُصُوص", "شِبِين القناطر",)))
            city.add(City("الإسكندرية", listOf("الإسكندرية", "برج العرب", "برج العرب الجديدة",)))
            city.add(City("البحيرة", listOf("دَمَنْهور", "كفر الدَّوَّار", "رَشيد", "إدكو", "أبو المطامير", "أبو حُمُّص", "الدِّلنْجات", "المحموديّة", "الرحمانيّة", "إيتاي البارود", "حُوش عيسى", "شُبراخِيت", "كوم حمادة", "بدر", "وادي النَطْرون", "النُوبَاريّة الجديدة",)))
            city.add(City("مطروح", listOf("مَرْسَى مَطْرُوح", "الحَمَّام", "العَلَمِين", "الضَّبْعَة", "النِّجِيلَة", "سِيِدي بَرَّانِي", "السَّلُّوم", "سِيوَة")))
            city.add(City("دمياط",listOf("دمياط", "رأس البر", "فارسكور", "كفر سعد", "الزرقا", "السرو","الروضة", "كفر البطيخ", "عزبة البرج", "ميت أبو غالب",)))
            city.add(City("الدقهلية", listOf("المنصورة", "طَلْخا", "ميت غمر", "دِكِرِنْس", "أجا", "منية النصر", "السنبلاوين", "الكردي", "بني عبيد", "المنزلة", "تمي الأمديد", "الجمالية", "شربين", "المطرية", "بلقاس", "ميت سلسيل", "جمصة", "محلة دمنة", "نبروه",)))
            city.add(City("كفر الشيخ", listOf("كفر الشيخ", "دِسوق", "فُـوّه", "مِطوُبِس", "بَلْطيم", "مصيف بَلْطيم", "الحامول", "بِيَلا", "سيدي سالم", "قَلّين", "سيدي غازي", "بُرج البُرلُّس", "مِسِير",)))
            city.add(City("الغربية", listOf("طنْطَا", "المحلة الكبرى", "كفر الزَّيَّات", "زِفْتى", "السّنْطة", "قُطور", "بَسْيون", "سَمَنُّود",)))
            city.add(City("المنوفية",listOf("مدينة السادات", "مِنُوف", "سِرس الليَّان", "أَشْمُون", "الباجور", "قُوِيْسنا", "بركة السبع", "تَلَا", "الشهداء",)))
            city.add(City("الشرقية", listOf("العاشر من رمضان", "منيا القمح", "بِلْبيس", "أمشتول السوق", "القنايات", "أبو حَمّاد", "القُرين", "هِهْيا", "أبو كبير", "فاقوس", "ديرب نجم", "فاقوس", "الحسينية", "منشأة أبو عمر", "صان الحجر القبلية", "كفر صقر", "أولاد صقر", "الإبراهيمية", "الصالحية الجديدة",)))
            city.add(City("بورسعيد", listOf("بورسعيد", "بورفؤاد")))
            city.add(City("الإسماعيلية",listOf("الإسماعيلية", "فايد", "القنطرة شرق", "القنطرة غرب", "التل الكبير", "أبو صوير المحطة", "القصاصين الجديدة",)))
            city.add(City("السويس", listOf("الأربعين", "السويس", "فيصل", "الجناين", "عتاقة",)))
            city.add(City("شمال سيناء", listOf("العريش", "الشّيخ زُوَيِّد", "رَفَح", "بئر العبد", "الحَسَنَة", "نَخِل",)))
            city.add(City("الفيوم", listOf("الفيوم", "الفُيُّوم الجديدة", "طَامِيِّة", "إبشواي", "يوسف الصديق", "سنورس", "إطسا")))
            city.add(City("المنيا", listOf("المنيا", "المنيا الجديدة", "العدوة", "مَغَاغَة", "بني مزار", "مَطَاي", "سَمَالُوط", "المدينة الفكرية", "مَلّوي", "دِير مَوَاس",)))
            city.add(City("أسيوط", listOf("أسيوط", "أسيوط الجديدة", "دَيْرُوط", "مَنْفَلوط", "القوصية", "أَبْنُوب", "أبو تيج", "الغنايم", "ساحل سليم", "البداري", "صِدفا",)))
            city.add(City("الوادي الجديد",listOf("الخَارْجَة", "باريس", "مُوط", "الفرافرة", "بلاط",)))
            city.add(City("سوهاج", listOf("سُوهَاج", "سوهاج الجديدة", "أخميم", "أخميم الجديدة", "البلْيَنا", "المراغة", "بلاط", "المنشأة", "دار السلام", "جِرجا", "جُهِينَة الغربيّة", "ساقلتة", "طمَا", "طَهُطَا",)))
            city.add(City("قنا", listOf("قِنَا", "قنا الجديدة", "أبُو تِشْت", "نَجْع حَمَّادِي", "دِشْنَا", "الوَقْف", "قِفْط", "نَقَادَة", "قُوص", "فَرْشُوط",)))
            city.add(City("الأقصر", listOf("الأقصر", "الأقصر الجديدة", "طِيبة الجديدة", "الزينيّة", "البَيَاضِيّة", "القُرْنَة", "أَرمَنْت", "الطُّود", "إسنا",)))
            city.add(City("أسوان", listOf("أَسْوان", "أَسْوان الجديدة", "دراو", "كُوم أُمْبُو", "نصر النوبة", "كَلَابْشَة", "إِدفو", "الرِّدِيسيّة", "البِصِيليَّة", "السباعية", "أبو سمبل السياحية",)))
            return city
        }
    }
}