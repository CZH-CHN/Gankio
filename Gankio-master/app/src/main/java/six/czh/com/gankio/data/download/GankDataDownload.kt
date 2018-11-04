package six.czh.com.gankio.data.download

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Looper
import android.os.StatFs
import android.support.v4.content.ContextCompat
import android.util.Log
import com.bumptech.glide.Glide
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import six.czh.com.gankio.GankApplication
import six.czh.com.gankio.data.ApiService
import six.czh.com.gankio.data.GankioService
import six.czh.com.gankio.util.AppExecutors
import six.czh.com.gankio.util.LogUtils
import java.io.File

/**
 * Created by cai on 18-10-22.
 * Email: baicai94@foxmail.com
 */
class GankDataDownload(private val executor: AppExecutors): GankDataDownloadSource {
    /**
     * 下载文件实际方法
     * 1.检查SD卡权限
     * 2.检查文件夹是否存在
     */
    override fun downloadImage(uri: String, callback: GankDataDownloadSource.LoadGankDownloadCallback) {
        if(ContextCompat.checkSelfPermission(GankApplication.mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            callback.onGankDataDownloadedFail(DOWNLOAD_PERMISSION_DENIED)
            return
        }

       //创建文件夹 ./Picture/Gank Fuli
        val albumDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli")
        if (!albumDir.exists() || !albumDir.isDirectory) {
            if (!albumDir.mkdirs()) {
                callback.onGankDataDownloadedFail(DOWNLOAD_DIR_NO_EXISTED)
            }
        }



        executor.diskIO.execute {
            //判断图片是否已经在缓存目录中
            try {
                val cacheFile = Glide.with(GankApplication.mContext).load(uri).downloadOnly(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,
                        com.bumptech.glide.request.target.Target.SIZE_ORIGINAL).get()
                if (cacheFile.exists()) {
                    //如果文件已经存在则复制到上面的目录中
                    //文件名
                    val fileName = uri.substring(uri.lastIndexOf('/') + 1)

                    val file = File(albumDir.absolutePath + File.separator + fileName)
                    if (file.exists()) {
                        //文件已经存在无需再次下载
                        executor.mainThread.execute {
                            callback.onGankDataDownloadedFail(DOWNLOAD_FILE_IS_EXISTS)
                        }

                        return@execute
                    } else {
                        //复制文件
                        copyFile(cacheFile, file)
                        executor.mainThread.execute {
                            callback.onGankDataDownloaded()
                        }
                        return@execute
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                executor.mainThread.execute {
                    callback.onGankDataDownloadedFail(DOWNLOAD_NETWORK_ERROR)
                }
                return@execute
            }


            //下载图片
            val call = ApiService.createDownloadRetrofit().create(GankioService::class.java)
            val downloadCall = call.downloadImage(uri)

            downloadCall.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    callback.onGankDataDownloadedFail(DOWNLOAD_NETWORK_ERROR)
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                    LogUtils.d("Download onResponse Thread " + (Looper.myLooper() == Looper.getMainLooper()))
                    //文件名
                    val fileName = uri.substring(uri.lastIndexOf('/') + 1)

                    val file = File(albumDir.absolutePath + File.separator + fileName)

                    executor.diskIO.execute {
                        val success = writeStreamToFile(file, response)
                        executor.mainThread.execute {
                            when(success) {
                                true -> callback.onGankDataDownloaded()
                                false -> callback.onGankDataDownloadedFail(DOWNLOAD_WRITE_FILE_ERROR)
                            }
                        }
                    }

                }

            })

        }




    }


    fun writeStreamToFile(file: File,response: Response<ResponseBody>): Boolean {

        val body = response.body()
        if (checkUsableSpace(Environment.getExternalStorageDirectory()) < body!!.contentLength()) {
            return false
        }

        val stream = body.byteStream()

        //TODO 空指针问题
        return try {
            file.writeBytes(stream!!.readBytes(1024))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            stream?.close()
        }

    }

    fun checkUsableSpace(path: File): Long {
        val stats = StatFs(path.getPath())
        return stats.blockSize.toLong() * stats.availableBlocks.toLong()
    }

    fun copyFile(file: File, targetFile: File) {
        targetFile.writeBytes(file.readBytes())
    }


    fun checkAlbumStorgeDir() {
        if (isExternalStorageWritable()) {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "")

        }

    }

//    class photoDownloadTask: AsyncTask<String, Void, Int>() {
//        override fun doInBackground(vararg uri: String?): Int {
//            //判断图片是否已经在缓存目录中
//            val cacheFile = Glide.with(GankApplication.mContext).load(uri).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
//            if (cacheFile.exists()) {
//                //如果文件已经存在则复制到上面的目录中
//                return -1
//            }
//
//            return 1
//        }
//
//        override fun onPostExecute(result: Int?) {
//            super.onPostExecute(result)
//        }
//
//
//    }

    private fun isExternalStorageWritable(): Boolean = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    companion object {

        private lateinit var INSTANCE: GankDataDownload
        private var needsNewInstance = true

        @JvmStatic fun getInstance(executor: AppExecutors): GankDataDownload {
            if (needsNewInstance) {
                INSTANCE = GankDataDownload(executor)
                needsNewInstance = false
            }
            return INSTANCE
        }
    }


    fun downloadImage(uri: String, type: Int, callback: GankDataDownloadSource.LoadGankDownloadCallback) {
        if(ContextCompat.checkSelfPermission(GankApplication.mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            callback.onGankDataDownloadedFail(DOWNLOAD_PERMISSION_DENIED)
            return
        }

        //创建文件夹 ./Picture/Gank Fuli
        val albumDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gank Fuli")
        if (!albumDir.exists() || !albumDir.isDirectory) {
            if (!albumDir.mkdirs()) {
                callback.onGankDataDownloadedFail(DOWNLOAD_DIR_NO_EXISTED)
            }
        }



        executor.diskIO.execute {
            //判断图片是否已经在缓存目录中
            try {
                val cacheFile = Glide.with(GankApplication.mContext).load(uri).downloadOnly(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,
                        com.bumptech.glide.request.target.Target.SIZE_ORIGINAL).get()
                if (cacheFile.exists()) {
                    //如果文件已经存在则复制到上面的目录中
                    //文件名
                    val fileName = uri.substring(uri.lastIndexOf('/') + 1)

                    val file = File(albumDir.absolutePath + File.separator + fileName)
                    if (file.exists()) {
                        //文件已经存在无需再次下载
                        executor.mainThread.execute {
                            callback.onGankDataDownloadedFail(DOWNLOAD_FILE_IS_EXISTS)
                        }

                        return@execute
                    } else {
                        //复制文件
                        copyFile(cacheFile, file)
                        executor.mainThread.execute {
                            callback.onGankDataDownloaded()
                        }
                        return@execute
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                executor.mainThread.execute {
                    callback.onGankDataDownloadedFail(DOWNLOAD_NETWORK_ERROR)
                }
                return@execute
            }


            //下载图片
            val call = ApiService.createDownloadRetrofit().create(GankioService::class.java)
            val downloadCall = call.downloadImage(uri)

            downloadCall.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    callback.onGankDataDownloadedFail(DOWNLOAD_NETWORK_ERROR)
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                    LogUtils.d("Download onResponse Thread " + (Looper.myLooper() == Looper.getMainLooper()))
                    //文件名
                    val fileName = uri.substring(uri.lastIndexOf('/') + 1)

                    val file = File(albumDir.absolutePath + File.separator + fileName)

                    executor.diskIO.execute {
                        val success = writeStreamToFile(file, response)
                        executor.mainThread.execute {
                            when(success) {
                                true -> callback.onGankDataDownloaded()
                                false -> callback.onGankDataDownloadedFail(DOWNLOAD_WRITE_FILE_ERROR)
                            }
                        }
                    }

                }

            })

        }
    }
}