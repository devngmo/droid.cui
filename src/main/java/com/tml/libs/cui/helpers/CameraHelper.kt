package com.tml.libs.cui.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.media.ExifInterface
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.tml.libs.cutils.StaticLogger
import java.io.File
import java.io.FileNotFoundException


class CameraHelper {
    companion object {
        @JvmStatic fun takePhoto(a : Activity, requestCode:Int) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(a.packageManager) != null) {
                a.startActivityForResult(intent, requestCode)
            }
        }

        @JvmStatic fun getImageFromResult(data:Intent?):Bitmap? {
            data?.let {
                val extra = it.extras
                extra?.let {
                    return extra.get("data") as Bitmap
                }
            }
            return null
        }


        @JvmStatic fun rotate(bm: Bitmap, rotation: Int): Bitmap {
            if (rotation != 0) {
                val matrix = Matrix()
                matrix.postRotate(rotation.toFloat())
                return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
            }
            return bm
        }

        @JvmStatic
        fun decodeBitmap(context: Context, theUri: Uri, sampleSize: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inSampleSize = sampleSize

            var fileDescriptor: ParcelFileDescriptor? = null
            try {
                fileDescriptor = context.contentResolver.openFileDescriptor(theUri, "r")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return BitmapFactory.decodeFileDescriptor(
                fileDescriptor!!.fileDescriptor, null, options
            )
        }

        @JvmStatic val minWidthQuality = 128
        @JvmStatic fun getImageResized(context: Context, selectedImage: Uri): Bitmap {
            var bm: Bitmap?
            val sampleSizes = intArrayOf(64,32,24,16,8,4, 3, 2, 1)
            var i = 0
            do {
                bm = decodeBitmap(context, selectedImage, sampleSizes[i])
                i++
            } while (bm!!.width < minWidthQuality && i < sampleSizes.size)
            return bm
        }

        @JvmStatic
        fun getThumbnail(file: File, thumbnailWidth: Int): Bitmap? {
            StaticLogger.D("CameraHelper", "getThumbnail $file")
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
//            var fileDescriptor: ParcelFileDescriptor? = null
//            try {
//                fileDescriptor = context.contentResolver.openFileDescriptor(theUri, "r")
//                StaticLogger.D("CameraHelper", "fileDescriptor " + fileDescriptor.fileDescriptor.toString())
//            } catch (e: FileNotFoundException) {
//                StaticLogger.E("CameraHelper", "exception when open file", e)
//            }

            //val bmpInfo = BitmapFactory.decodeFile(file.absolutePath, options)
            //StaticLogger.D(this, "bmpInfo ${options.outWidth} x ${options.outHeight}")

            var sampleSize = 1
            if (options.outWidth > thumbnailWidth)
                sampleSize = (options.outWidth * 1.0f / thumbnailWidth).toInt()
            options.inJustDecodeBounds = false
            options.inSampleSize = sampleSize
            StaticLogger.D(this, "sample size $sampleSize")
            return BitmapFactory.decodeFile(
                file.absolutePath,
                //fileDescriptor!!.fileDescriptor, null,
                options
            )
        }

        @JvmStatic fun getRotationFromCamera(context: Context, imageFile: Uri): Int {
            var rotate = 0
            try {
                context.contentResolver.notifyChange(imageFile, null)
                val exif = ExifInterface(imageFile.path!!)
                val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL)

                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return rotate
        }

        @JvmStatic fun getThumbnailOfCapturedPhoto(context:Context, cacheFile: File, thumbnailWidth:Int): Bitmap? {
            StaticLogger.D("CameraHelper", "getThumbnailOfCapturedPhoto...")
            try {
                if (!cacheFile.exists()) {
                    StaticLogger.D("CameraHelper", " cache file not exist: " + cacheFile.absolutePath)
                    return null
                }

                val capturedCacheFileUri = Uri.fromFile(cacheFile)
                StaticLogger.D("CameraHelper", "uri  $capturedCacheFileUri")
                var bmp = //CameraHelper.getImageResized(context, capturedCacheFileUri)
                        getThumbnail(cacheFile, thumbnailWidth)
                bmp?.let {
                    StaticLogger.D("CameraHelper", "thumbnail size " + it.width)
                    //getImageResized(getContext(), capturedCacheFileUri);
                    val rotation = getRotationFromCamera(context, capturedCacheFileUri)
                    bmp = rotate(it, rotation)
                }
                return bmp
            } catch (ex: Exception) {
                StaticLogger.E(ex)
            }
            return null
        }

        @JvmStatic fun getBitmapFromCameraCapture(data: Intent?): Bitmap? {
            if (data == null) {
                StaticLogger.E(this, "camera data is null")
                return null
            }
// if (bmp == null) {
            // Uri uri = data.getData();
            // try {
            // bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            // return bmp;
            // } catch (IOException e) {
            // StaticLogger.E(e);
            // }
            // }
            return data.extras!!.get("data") as Bitmap
        }
    }
}