package com.example.filebrowserandroid.ui.filebrowser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.FileInfo
import com.example.filebrowserandroid.databinding.FragmentFilesBrowserBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


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
                            is FileBrowserScreenEvent.NavigateToTextDirectory -> {
                                val action =
                                    FileBrowserFragmentDirections.actionFilesBrowserFragmentSelf(
                                        event.path
                                    )
                                findNavController().navigate(action)
                            }
                            is FileBrowserScreenEvent.ShowSnackBarMessage -> {
                                Snackbar.make(
                                    requireView(),
                                    event.message.asString(requireContext()),
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                }


            }

        }

    }

    override fun onFileClick(file: FileInfo) {
        TODO("Not yet implemented")
    }
}

