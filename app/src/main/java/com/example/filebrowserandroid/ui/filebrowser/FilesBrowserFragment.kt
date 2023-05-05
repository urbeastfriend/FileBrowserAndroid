package com.example.filebrowserandroid.ui.filebrowser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.filebrowserandroid.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilesBrowserFragment : Fragment(R.layout.fragment_files_browser) {

    private val viewModel: FilesBrowserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}