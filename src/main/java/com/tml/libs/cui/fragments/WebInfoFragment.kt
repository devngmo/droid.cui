package com.tml.libs.cui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tml.libs.cui.R
import kotlinx.android.synthetic.main.fragment_web_info.*

open class WebInfoFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reloadContent()
    }

    open fun reloadContent() {
        val html = generateContentTML()
        wif_webview.loadData(html, "text/html", "utf-8")
    }

    open fun generateContentTML(): String {
        var html = ""
        return html
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            WebInfoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
