package com.zaqly.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaqly.storyapp.databinding.ActivityAdapterBinding
import com.zaqly.storyapp.network.response.ListStoryItem

class AdapterActivity(
    private val onItemClick: (ListStoryItem, ImageView, TextView) -> Unit
) : RecyclerView.Adapter<AdapterActivity.StoryViewHolder>() {

    private val stories = mutableListOf<ListStoryItem>()

    fun setStories(newStories: List<ListStoryItem>) {
        stories.clear()
        stories.addAll(newStories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ActivityAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size
    inner class StoryViewHolder(private val binding: ActivityAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView = binding.ivImg
        val textView = binding.tvListname

        fun bind(story: ListStoryItem) {
            binding.tvListname.text = story.name
            binding.tvListDeskripsi.text = story.description

            Glide.with(binding.ivImg.context)
                .load(story.photoUrl)
                .into(binding.ivImg)

            binding.root.setOnClickListener {
                onItemClick(story, binding.ivImg, binding.tvListname)
            }
        }
    }
}
