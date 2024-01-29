package com.example.android.testnearbyimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter(context: Context, private val mData: List<ImageData>) :
  RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

  private var mInflater: LayoutInflater

  init {
    mInflater = LayoutInflater.from(context)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = mInflater.inflate(R.layout.recyclerview_row, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = mData[position]
    holder.imageView.setImageBitmap(data.bitmap)
    holder.textView.text = (data.fileSize / 1024).toString() + "KB"
  }

  override fun getItemCount(): Int {
    return mData.size
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val imageView: ImageView
    val textView: TextView

    init {
      imageView = itemView.findViewById(R.id.imageview)
      textView = itemView.findViewById(R.id.imageText)
    }

  }
}
