package com.example.filebrowserandroid.ui.filebrowser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.FileInfo
import com.example.filebrowserandroid.databinding.ItemFileBinding
import com.example.filebrowserandroid.common.contains
import java.io.File


class FileRVAdapter(private val listener: OnFileClickListener) :
    ListAdapter<FileInfo, FileRVAdapter.FileViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class FileViewHolder(private val binding: ItemFileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val file = getItem(position)
                        listener.onFileClick(file)
                    }
                }
            }
        }

        fun bind(file: FileInfo) {
            val _file = File(file.filePath)

            if (_file.exists()) {
                binding.apply {
                    val viewContext = fileImage.context
                    fileName.text = file.fileName

                    fileDateCreated.text = file.dateCreated
                    if (_file.isDirectory) {
                        fileImage.setImageResource(R.drawable.ic_folder)
                    } else {
                        val fileSizeText = "${android.text.format.Formatter.formatFileSize(viewContext,file.fileSize)} | "
                        fileSize.text =  fileSizeText
                        when (_file.extension) {
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
    }

    interface OnFileClickListener {

        fun onFileClick(file: FileInfo)

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