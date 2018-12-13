package dema.im.audioservice.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.graphics.BitmapCompat
import android.util.Log
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImageLoader(context: Context) {

    val CACHE_PATH = "${context.cacheDir.absolutePath}/art/".also {
        val file = File(it)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    fun isCacheFileExist(fileName: String): Boolean {
        return File(CACHE_PATH + fileName).exists()
    }

    val client = OkHttpClient.Builder().build()

    fun getFileName(url: String): String {
        val fileName = url.substring(url.lastIndexOf('/') + 1, url.length)
        return fileName
    }


    fun load(url: String) {
        val fileName = getFileName(url)
        if (!isCacheFileExist(fileName)) {
            client.newCall(Request.Builder().url(url).build()).enqueue(Callback(fileName))
        }
    }

    inner class Callback(val fileName: String) : okhttp3.Callback {

        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val file = File(CACHE_PATH + fileName)
                if (!file.exists()) {
                    file.createNewFile()
                    val fos = FileOutputStream(file)
                    fos.write(response.body()?.bytes())
                    fos.close()
                    Log.d(TAG, "loaded: " + fileName)
                }
            }
        }
    }

    fun getDrawable(uri: Uri): Bitmap {
        val fileName = getFileName(uri.toString())
        val fullImagePath = CACHE_PATH + fileName
        return BitmapFactory.decodeFile(fullImagePath)
    }

    fun getDrawable(url: String): Bitmap {
        val fileName = getFileName(url)
        val fullImagePath = CACHE_PATH + fileName
        return BitmapFactory.decodeFile(fullImagePath)
    }

}

const val TAG = "Loader"