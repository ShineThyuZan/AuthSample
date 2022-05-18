package com.galaxytechno.chat.util

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

object TypeConverter {

    fun createPartFromString(
        str: String?
    ) = (str ?: "").toRequestBody(MultipartBody.FORM)

    fun createPartFile(
        context: Context,
        partName: String,
        fileUri: Uri
    ): MultipartBody.Part {
        val file = FileUtils.getFile(context, fileUri)
        val requestFile =
            file!!.asRequestBody(context.contentResolver.getType(fileUri)!!.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    fun createPartFile(
        context: Context,
        partName: String,
        fileUri: Uri,
        file: File
    ): MultipartBody.Part {
        val requestFile =
            file.asRequestBody(context.contentResolver.getType(fileUri)!!.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


    fun createPartFile(
        partName: String,
        file: File
    ): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


    fun createPartFile(
        partName: String,
        bytes: ByteArray
    ): MultipartBody.Part {
        val rb = bytes.toRequestBody("image/jpeg".toMediaType())
        val fileType = MultipartBody.Part.createFormData(partName, "image.jpg", rb)
        return MultipartBody.Part.createFormData(partName, "image.jpg", rb)
    }

    fun createPartFileNullable(
        partName: String,
    ): MultipartBody.Part {
        val body = "".toRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData(partName, "", body)
    }

}