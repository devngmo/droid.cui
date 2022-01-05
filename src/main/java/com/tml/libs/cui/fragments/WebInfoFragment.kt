package com.tml.libs.cui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tml.libs.cui.R

open class WebInfoFragment : Fragment() {
    lateinit var wif_webview: WebView
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
        val root = inflater.inflate(R.layout.fragment_web_info, container, false)
        wif_webview = root.findViewById(R.id.wif_webview)
        return root
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
