package com.example.filebrowserandroid.ui.modifiedfileslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.FileInfo
import com.example.filebrowserandroid.common.contains
import com.example.filebrowserandroid.databinding.ModifiedItemFileBinding
import java.io.File

class ModifiedFileRVAdapter :
    ListAdapter<FileInfo, ModifiedFileRVAdapter.FileViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding =
            ModifiedItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class FileViewHolder(private val binding: ModifiedItemFileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(_file: FileInfo) {
            val file = File(_file.filePath)

            if (file.exists()) {
                binding.apply {

                    fileName.text = _file.fileName
                    fileDateCreated.text = _file.dateCreated

                    when (file.extension) {
                        in Regex("((?i)(gif|png|jpeg|jpg))$") -> {
                            fileImage.setImageResource(R.drawable.ic_image)
                        }
                        else -> {
                            fileImage.setImageResource(R.drawable.ic_file)
                        }
                    }

                }
            }
        }
    }


    class DiffCallBack : DiffUtil.ItemCallback<FileInfo>() {
        override fun areItemsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem == newItem
        }
    }


}