package com.example.filebrowserandroid.ui.filebrowser

import android.content.ActivityNotFoundException
import android.content.Intent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.filebrowserandroid.BuildConfig
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.FileInfo
import com.example.filebrowserandroid.common.SortOrder
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


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

            toolbar.inflateMenu(R.menu.browser_menu)

            toolbar.setNavigationOnClickListener{
                findNavController().popBackStack()
            }


            toolbar.setOnMenuItemClickListener{
                when (it.itemId) {
                    R.id.action_sort_by_name -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                        true
                    }
                    R.id.action_sort_by_size_asc -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_SIZE_ASC)
                        true
                    }
                    R.id.action_sort_by_size_desc -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_SIZE_DESC)
                        true
                    }
                    R.id.action_sort_by_date_created -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_DATE_CREATED)
                        true
                    }
                    R.id.action_sort_by_extension -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_EXTENSION)
                        true
                    }
                    else -> false
            }}
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
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    val file = File(event.path)
                                    val uriFromFile = FileProvider.getUriForFile(
                                        requireContext(),
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        file
                                    )
                                    intent.setDataAndType(uriFromFile, file.getMimeType())
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
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
                            is FileBrowserScreenEvent.ShareFile -> {
                                try {
                                    val file = File(event.path)
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "*/*"
                                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        putExtra(Intent.EXTRA_SUBJECT,"Sharing file")
                                        putExtra(Intent.EXTRA_TEXT,"Sharing file from file browser app")

                                        val uriFromFile = FileProvider.getUriForFile(
                                            requireContext(),
                                            BuildConfig.APPLICATION_ID + ".provider",
                                            file
                                        )

                                        putExtra(Intent.EXTRA_STREAM,uriFromFile)
                                    }

                                    startActivity(intent)
                                }
                                catch (e: Exception){
                                    e.message?.let { Log.d("random exception", it) }
                                }
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

    override fun onFileLongClick(file: FileInfo) {
        viewModel.onLongClick(file.filePath)
    }
}

