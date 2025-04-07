package com.example.pix.ui.imagesList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pix.R
import com.example.pix.databinding.ItemImageBinding
import com.example.pix.domain.entity.Picture

class ImagesListAdapter(private var dataset: List<Picture>) :
    RecyclerView.Adapter<ImagesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(picture: Picture)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImage: ImageView = binding.ivPicture
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(ItemImageBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionData = dataset[position]
        val context = holder.itemView.context

        val width = (context.resources.displayMetrics.widthPixels * 0.5).toInt()
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = width
        layoutParams.width = width
        holder.itemView.layoutParams = layoutParams

        Glide.with(context)
            .load(positionData.url)
            .error(R.drawable.img_error)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.itemImage)

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(positionData)
        }
    }

    fun setDataset(newDataset: List<Picture>) {
        dataset = newDataset
    }
}