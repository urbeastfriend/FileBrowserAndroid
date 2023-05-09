package com.example.filebrowserandroid.ui.filebrowser

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filebrowserandroid.BuildConfig
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.FileInfo
import com.example.filebrowserandroid.databinding.FragmentFilesBrowserBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import com.example.filebrowserandroid.common.getMimeType


@AndroidEntryPoint
class FileBrowserFragment : Fragment(R.layout.fragment_files_browser),
    FileRVAdapter.OnFileClickListener {

    private val viewModel: FileBrowserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFilesBrowserBinding.bind(view)

        val fileAdapter = FileRVAdapter(this)


        binding.apply {
            filesRecyclerView.apply {
                adapter = fileAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                isNestedScrollingEnabled = true
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // observing state & updating ui
                launch {
                    viewModel.screenState.collect { state ->
                        binding.pathText.text = state.path
                        if (state.isLoading) { // show skeletons while loading data
                            binding.isLoadingProgressBar.visibility = View.VISIBLE
                        } else {
                            binding.isLoadingProgressBar.visibility = View.GONE
                            if (state.fileList.isNotEmpty()) {
                                fileAdapter.submitList(state.fileList)
                            }
                            // file list is empty, show snackbar
                            else {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.noFilesFound),
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                }

                // listening events
                launch {
                    viewModel.eventChannel.collect { event ->
                        when (event) {
                            is FileBrowserScreenEvent.OpenFile -> {
                                try{
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    val file = File(event.path)
                                    val uriFromFile = FileProvider.getUriForFile(requireContext(),BuildConfig.APPLICATION_ID +".provider",file)
                                    intent.setDataAndType(uriFromFile,file.getMimeType())
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(intent)
                                }
                                catch (e: ActivityNotFoundException){
                                    Snackbar.make(
                                        requireView(),
                                        R.string.couldntOpenFile,
                                        Snackbar.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                            is FileBrowserScreenEvent.ShowSnackBarMessage -> {
                                Snackbar.make(
                                    requireView(),
                                    event.message.asString(requireContext()),
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                            is FileBrowserScreenEvent.NavigateToDirectory -> {
                                val action =
                                    FileBrowserFragmentDirections.actionFilesBrowserFragmentSelf(
                                        event.path
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    }
                }


            }

        }

    }

    override fun onFileClick(file: FileInfo) {
        viewModel.onFileClick(file.filePath)
    }
}

