package com.example.filebrowserandroid.ui.modifiedfileslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.databinding.FragmentModifiedFilesListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ModifiedFileListFragment : Fragment(R.layout.fragment_modified_files_list) {

    private val viewModel: ModifiedFileListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentModifiedFilesListBinding.bind(view)

        val modifiedFilesAdapter = ModifiedFileRVAdapter()

        binding.apply {
            modifiedFilesRecyclerView.apply {
                adapter = modifiedFilesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                isNestedScrollingEnabled = true
            }
        }



        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel.screenState.collect{state->
                    if(state.isLoading){
                        binding.isLoadingProgressBar.visibility = View.VISIBLE
                    }
                    else{
                        binding.isLoadingProgressBar.visibility = View.GONE

                        if(state.fileList.isNotEmpty()){
                            modifiedFilesAdapter.submitList(state.fileList)
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
        }
    }
}