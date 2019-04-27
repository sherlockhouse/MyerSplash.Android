package com.juniperphoton.myersplash.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.juniperphoton.myersplash.App
import com.juniperphoton.myersplash.R
import com.juniperphoton.myersplash.db.AppDatabase
import com.juniperphoton.myersplash.event.DownloadStartedEvent
import com.juniperphoton.myersplash.extension.usingWifi
import com.juniperphoton.myersplash.model.DownloadItem
import com.juniperphoton.myersplash.model.UnsplashImage
import com.juniperphoton.myersplash.service.DownloadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream

@Suppress("unused_parameter")
object DownloadUtil {
    private const val TAG = "DownloadUtil"

    /**
     * Write [body] to a file of [fileUri].
     * @param onProgress will be invoked when the progress has been updated.
     */
    suspend fun writeToFile(body: ResponseBody,
                            fileUri: String,
                            onProgress: ((Int) -> Unit)?): File? = withContext(Dispatchers.IO) {
        return@withContext try {
            val fileToSave = File(fileUri)

            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(fileToSave)

            inputStream.use { `is` ->
                outputStream.use { os ->
                    val buffer = ByteArray(2048)

                    val fileSize = body.contentLength()
                    var fileSizeDownloaded: Long = 0

                    var progressToReport = 0

                    while (true) {
                        val read = `is`.read(buffer)
                        if (read == -1) {
                            break
                        }

                        os.write(buffer, 0, read)
                        fileSizeDownloaded += read.toLong()

                        val progress = (fileSizeDownloaded / fileSize.toDouble() * 100).toInt()
                        if (progress - progressToReport >= 5) {
                            progressToReport = progress
                            onProgress?.invoke(progressToReport)
                        }
                    }

                    fileToSave
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get file to save given a [expectedName].
     */
    fun getFileToSave(expectedName: String): File? {
        val galleryPath = FileUtil.galleryPath ?: return null
        val folder = File(galleryPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return File(folder.toString() + File.separator + expectedName)
    }

    /**
     * Cancel the download of specified [image].
     */
    fun cancelDownload(context: Context, image: UnsplashImage) {
        val intent = Intent(App.instance, DownloadService::class.java)
        intent.putExtra(Params.CANCELED_KEY, true)
        intent.putExtra(Params.URL_KEY, image.downloadUrl)
        context.startService(intent)
    }

    /**
     * Start downloading the [image].
     * @param context used to check network status
     */
    fun download(context: Context, image: UnsplashImage) {
        var previewFile: File? = null
        image.listUrl?.let {
            previewFile = FileUtil.getCachedFile(it)
        }
        DownloadReporter.report(image.downloadLocationLink)
        startDownloadService(context, image.fileNameForDownload, image.downloadUrl!!, previewFile?.path)
        persistDownloadItem(context, image)
        EventBus.getDefault().post(DownloadStartedEvent(image.id))
        Toaster.sendShortToast(context.getString(R.string.downloading_in_background))
    }

    private fun persistDownloadItem(context: Context, image: UnsplashImage) {
        GlobalScope.launch(Dispatchers.IO) {
            val item = DownloadItem(image.id!!, image.listUrl!!, image.downloadUrl!!,
                    image.fileNameForDownload)
            item.color = image.themeColor
            AppDatabase.instance.downloadItemDao().insertAll(item)
        }
    }

    private fun startDownloadService(context: Context, name: String, url: String, previewUrl: String? = null) {
        val intent = Intent(context, DownloadService::class.java)
        intent.putExtra(Params.NAME_KEY, name)
        intent.putExtra(Params.URL_KEY, url)
        previewUrl?.let {
            intent.putExtra(Params.PREVIEW_URI, previewUrl)
        }
        context.startService(intent)
    }
}
