package com.tml.libs.cui.activities

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.tml.libs.cui.JSONInputDialog
import com.tml.libs.cui.R
import kotlinx.android.synthetic.main.image_viewer_activity.*

val ARG_IMAGE_FILE_URI = "img.file.uri"
val ARG_IMAGE_FILE_TITLE = "img.file.title"
class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_viewer_activity)
        var fileURI  = ""
        var fileTitle  = ""
        try {
            fileURI = intent.extras.getString(ARG_IMAGE_FILE_URI)
            fileTitle = intent.extras.getString(ARG_IMAGE_FILE_TITLE)
            var bmp = loadBitmap(fileURI)
            ivaImg.setImageBitmap(bmp)
            ivaTitle.setText(fileTitle)
        }
        catch (ex:Exception) {
            JSONInputDialog.createMessageBoxOK(this, "Lỗi mở file ảnh", "Không mở được file: ${fileURI}", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                    finish()
                }
            })
        }
    }

    private fun loadBitmap(fileURI: String?): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(fileURI, options)
    }
}