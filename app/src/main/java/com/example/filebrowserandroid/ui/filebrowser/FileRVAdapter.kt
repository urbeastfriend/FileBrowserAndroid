package com.example.filebrowserandroid.ui.filebrowser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.FileInfo
import com.example.filebrowserandroid.common.contains
import com.example.filebrowserandroid.databinding.BrowserItemFileBinding
import java.io.File


class FileRVAdapter(private val listener: OnFileClickListener) :
    ListAdapter<FileInfo, FileRVAdapter.FileViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = BrowserItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class FileViewHolder(private val binding: BrowserItemFileBinding) :
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
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val file = getItem(position)
                        listener.onFileLongClick(file)

                    }
                    true
                }
            }
        }

        fun bind(_file: FileInfo) {
            val file = File(_file.filePath)

            if (file.exists()) {
                binding.apply {
                    val viewContext = fileImage.context
                    fileName.text = _file.fileName

                    fileDateCreated.text = _file.dateCreated
                    if (file.isDirectory) {
                        fileImage.setImageResource(R.drawable.ic_folder)
                    } else {
                        val fileSizeText = "${android.text.format.Formatter.formatFileSize(viewContext,_file.fileSize)} | "
                        fileSize.text =  fileSizeText
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
    }

    interface OnFileClickListener {

        fun onFileClick(file: FileInfo)

        fun onFileLongClick(file: FileInfo)

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