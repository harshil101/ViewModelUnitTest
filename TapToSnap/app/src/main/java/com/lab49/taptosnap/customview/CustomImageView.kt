package com.lab49.taptosnap.customview

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.lab49.taptosnap.R
import com.lab49.taptosnap.databinding.CustomImageViewBinding
import java.io.File

data class CustomImageProperties(
    val imageURI: String? = null,
    val title: String? = null,
    val isMatched: Boolean? = null
)

class CustomImageView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var binding: CustomImageViewBinding =
        CustomImageViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView)
        binding.tileImg.setImageDrawable(attributes.getDrawable(R.styleable.CustomImageView_image))
        binding.imageNameTV.text = attributes.getString(R.styleable.CustomImageView_text)
        attributes.recycle()
    }

    fun setImageProperty(customImageProperties: CustomImageProperties) {
        customImageProperties.imageURI?.let {
            val uri = Uri.parse(it)
            binding.apply {
                tileImg.setImageURI(null)
                tileImg.setImageURI(uri)
            }
        }
        binding.apply {
            imageNameTV.text = customImageProperties.title
        }
        when (customImageProperties.isMatched) {
            true -> {
                binding.apply {
                    tileFrame.background = ContextCompat.getDrawable(context, R.drawable.tile_frame_green)
                    placeHolder.visibility = GONE
                    tryAgainTV.visibility = GONE
                }
            }
            false -> {
                binding.apply {
                    tileFrame.background = ContextCompat.getDrawable(context, R.drawable.tile_frame_red)
                    placeHolder.visibility = GONE
                    tryAgainTV.visibility = VISIBLE
                }
            }
            else -> {
                binding.tileFrame.background = ContextCompat.getDrawable(context, R.drawable.tile_frame)
            }
        }
    }
}