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
import android.widget.Toast
import com.tml.libs.cutils.StaticLogger
import java.io.File
import java.io.FileNotFoundException


public class CameraHelper {
    companion object {
        @JvmStatic fun takePhoto(a : Activity, requestCode:Int) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(a.getPackageManager()) != null) {
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
            return null;
        }


        @JvmStatic fun rotate(bm: Bitmap, rotation: Int): Bitmap {
            if (rotation != 0) {
                val matrix = Matrix()
                matrix.postRotate(rotation.toFloat())
                return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
            }
            return bm
        }

        @JvmStatic fun decodeBitmap(context: Context, theUri: Uri, sampleSize: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inSampleSize = sampleSize

            var fileDescriptor: ParcelFileDescriptor? = null
            try {
                fileDescriptor = context.contentResolver.openFileDescriptor(theUri, "r")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            val actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor!!.fileDescriptor, null, options)
            return actuallyUsableBitmap
        }

        @JvmStatic val minWidthQuality = 480
        @JvmStatic fun getImageResized(context: Context, selectedImage: Uri): Bitmap {
            var bm: Bitmap? = null
            val sampleSizes = intArrayOf(5, 3, 2, 1)
            var i = 0
            do {
                bm = decodeBitmap(context, selectedImage, sampleSizes[i])
                i++
            } while (bm!!.width < minWidthQuality && i < sampleSizes.size)
            return bm
        }

        @JvmStatic fun getThumbnail(context: Context, theUri: Uri, thumbnailWidth:Int): Bitmap? {
            StaticLogger.D("CameraHelper", "getThumbnail")
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.outWidth = 1
            options.outHeight = 1
            var fileDescriptor: ParcelFileDescriptor? = null
            try {
                fileDescriptor = context.contentResolver.openFileDescriptor(theUri, "r")
                StaticLogger.D("CameraHelper", "fileDescriptor " + fileDescriptor.fileDescriptor)
            } catch (e: FileNotFoundException) {
                StaticLogger.E(e)
            }

            val bmpInfo = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor!!.fileDescriptor, null, options)

            if (bmpInfo == null)
            {
                StaticLogger.E("CameraHelper", "can not get bmp info from file descriptor")
                return null
            }
            StaticLogger.D(this, "bmpInfo ${bmpInfo.width} x ${bmpInfo.height}")

            var sampleSize = bmpInfo.width / thumbnailWidth
            options.inJustDecodeBounds = false
            options.inSampleSize = sampleSize
            StaticLogger.D(this, "sample size $sampleSize")
            val thumbnail = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor!!.fileDescriptor, null, options)
            return thumbnail
        }

        @JvmStatic fun getRotationFromCamera(context: Context, imageFile: Uri): Int {
            var rotate = 0
            try {
                context.contentResolver.notifyChange(imageFile, null)
                val exif = ExifInterface(imageFile.getPath())
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

        @JvmStatic fun getThumbnailOfCapturedPhoto(context:Context, cacheFile: File): Bitmap? {
            StaticLogger.D("CameraHelper", "getThumbnailOfCapturedPhoto...")
            try {
                if (!cacheFile.exists()) {
                    StaticLogger.D("CameraHelper", " cache file not exist: " + cacheFile.absolutePath)
                    return null
                }

                val capturedCacheFileUri = Uri.fromFile(cacheFile)
                StaticLogger.D("CameraHelper", "uri  " + capturedCacheFileUri.toString())
                var bmp = CameraHelper.getImageResized(context, capturedCacheFileUri)
                        //CameraHelper.getThumbnail(context, capturedCacheFileUri, 100)
                bmp?.let {
                    StaticLogger.D("CameraHelper", "thumbnail size " + it.width)
                    //getImageResized(getContext(), capturedCacheFileUri);
                    val rotation = CameraHelper.getRotationFromCamera(context, capturedCacheFileUri)
                    bmp = CameraHelper.rotate(it, rotation)
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