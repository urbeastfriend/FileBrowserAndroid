package com.example.filebrowserandroid.ui.featurepicker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.databinding.FragmentFeaturePickerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeaturePickerFragment : Fragment(R.layout.fragment_feature_picker) {

    private val viewModel: FeaturePickerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFeaturePickerBinding.bind(view)

        binding.apply {
            featureFileBrowserBtn.setOnClickListener {
                viewModel.onNavigateFileBrowserClick()
            }

            featureModifiedFilesBtn.setOnClickListener {
                viewModel.onNavigateModifiedFilesClick()
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.eventChannel.collect{event->
                    when(event){
                        FeaturePickerScreenEvent.NavigateFileBrowserScreen -> {
                            val action = FeaturePickerFragmentDirections.actionFeaturePickerFragmentToFilesBrowserFragment(null)
                            findNavController().navigate(action)
                        }
                        FeaturePickerScreenEvent.NavigateModifiedFilesScreen -> {
                            val action = FeaturePickerFragmentDirections.actionFeaturePickerFragmentToModifiedFilesList()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }
}