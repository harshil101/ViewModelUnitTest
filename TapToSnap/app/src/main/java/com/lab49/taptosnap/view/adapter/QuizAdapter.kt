package com.lab49.taptosnap.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab49.taptosnap.customview.CustomImageProperties
import com.lab49.taptosnap.databinding.ItemQuizRowBinding
import com.lab49.taptosnap.network.models.QuizQuestionResponseItem
import com.lab49.taptosnap.util.ImageClickCallback

class QuizAdapter(
    private val list: ArrayList<QuizQuestionResponseItem>,
    private var imageClickCallback: ImageClickCallback
) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    private lateinit var binding: ItemQuizRowBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {

        binding = ItemQuizRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addQuestions(questions: List<QuizQuestionResponseItem>) {
        this.list.apply {
            clear()
            addAll(questions)
        }
    }

    fun addImageToView(
        position: Int,
        filePath: String,
        imgName: String,
        isMatched: Boolean? = null
    ) {
        this.list.removeAt(position)
        this.list.add(
            position, QuizQuestionResponseItem(
                name = imgName,
                imagePath = filePath,
                isMatched = isMatched
            )
        )
        notifyItemChanged(position)
    }

    inner class QuizViewHolder(private val binding: ItemQuizRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuizQuestionResponseItem) {
            binding.customView.setImageProperty(
                CustomImageProperties(
                    imageURI = question.imagePath,
                    title = question.name,
                    isMatched = question.isMatched
                )
            )
            binding.customView.setOnClickListener {
                question.name?.let { it1 -> imageClickCallback.onImageClick(adapterPosition, it1) }
            }
        }
    }
}