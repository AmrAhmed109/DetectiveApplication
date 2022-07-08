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
    }
}