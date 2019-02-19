@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package com.tml.libs.cui.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tml.libs.cui.JSONInputDialog
import com.tml.libs.cui.R
import kotlinx.android.synthetic.main.image_viewer_activity.*

const val ARG_IMAGE_FILE_URI = "img.file.uri"
const val ARG_IMAGE_FILE_TITLE = "img.file.title"
class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_viewer_activity)
        var fileURI  = ""
        try {
            fileURI = "" + intent.extras!!.getString(ARG_IMAGE_FILE_URI)
            val fileTitle = "" + intent.extras!!.getString(ARG_IMAGE_FILE_TITLE)
            val bmp = loadBitmap(fileURI)
            ivaImg.setImageBitmap(bmp)
            ivaTitle.text = fileTitle
        }
        catch (ex:Exception) {
            JSONInputDialog.createMessageBoxOK(this, "Lỗi mở file ảnh", "Không mở được file: $fileURI"
            ) { dialog, which ->
                dialog!!.dismiss()
                finish()
            }
        }
    }

    private fun loadBitmap(fileURI: String?): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(fileURI, options)
    }
}