package com.galaxytechno.chat.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.InputStream

object BitmapUtil {


    fun getBytes(bitmap: Bitmap?, percentage: Int = 70): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, percentage, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


    fun getBitmap(data: ByteArray?): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data!!.size)
    }


    fun getBitmap(stream: InputStream?): Bitmap {
        return BitmapFactory.decodeStream(stream)
    }

    fun getBitmap(uri: Uri, context: Activity): Bitmap {

        return when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            else -> {
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    uri
                )
            }
        }

    }

}