package com.example.detectiveapplication.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class Constants {
    companion object {
        const val BASE_URL = "https://krombo.000webhostapp.com/api/"
        const val IMAGE = "image"
        const val TIME_OUT = 5000.toLong()
        var TOKEN: String = ""
        val LOREM: String = "هذا النص هو مثال لنص يمكن أن يستبدل في نفس المساحة، لقد تم توليد هذا النص من مولد النص العربى، حيث يمكنك أن تولد مثل هذا النص أو العديد من النصوص الأخرى إضافة إلى زيادة عدد الحروف التى يولدها التطبيق. "
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
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData(
                if (key.isNullOrEmpty()) Constants.IMAGE else key,
                file.name,
                requestFile
            )
        }
    }
}