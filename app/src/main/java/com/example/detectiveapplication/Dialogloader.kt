package com.example.detectiveapplication

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.detectiveapplication.databinding.FragmentDialogLoaderBinding


class Dialogloader(context:Context) : Dialog(context) {

    private var _binding : FragmentDialogLoaderBinding? = null
    private val  binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dialog_loader)
//        setCancelable(false)
    }

}