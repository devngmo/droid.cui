package com.tml.libs.cui.factories

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface



/**
 * Created by TML on 16/03/2018.
 */
class DialogFactory {
    companion object {
        @JvmStatic fun createSingleChoiceDialog(c: Context, title:String, items:Array<String>, listener:DialogInterface.OnClickListener):AlertDialog {
            val mBuilder = AlertDialog.Builder(c)
            mBuilder.setTitle(title)
            mBuilder.setSingleChoiceItems(items, -1, listener)
            return mBuilder.create()
        }
    }
}