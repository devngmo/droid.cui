@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package com.tml.libs.cui.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tml.libs.cui.JSONInputDialog
import com.tml.libs.cui.R
import com.tml.libs.cui.databinding.ImageViewerActivityBinding

const val ARG_IMAGE_FILE_URI = "img.file.uri"
const val ARG_IMAGE_FILE_TITLE = "img.file.title"
class ImageViewerActivity : AppCompatActivity() {
    lateinit var _binding  : ImageViewerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ImageViewerActivityBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        var fileURI  = ""
        try {
            fileURI = "" + intent.extras!!.getString(ARG_IMAGE_FILE_URI)
            val fileTitle = "" + intent.extras!!.getString(ARG_IMAGE_FILE_TITLE)
            val bmp = loadBitmap(fileURI)
            _binding.ivaImg.setImageBitmap(bmp)
            _binding.ivaTitle.text = fileTitle
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