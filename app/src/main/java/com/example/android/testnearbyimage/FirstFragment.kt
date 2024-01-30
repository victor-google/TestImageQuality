package com.example.android.testnearbyimage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.testnearbyimage.databinding.FragmentFirstBinding
import java.io.ByteArrayOutputStream


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

  private var _binding: FragmentFirstBinding? = null
  private val binding get() = _binding!!

  lateinit var widthText: TextView
  lateinit var qualityText: TextView
  lateinit var refreshButton: Button
  lateinit var originalButton: Button
  lateinit var recyclerview: RecyclerView
  lateinit var originalImages: List<Bitmap>

  val widthToHeightRatio = 110f/153f

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    _binding = FragmentFirstBinding.inflate(inflater, container, false)
    widthText = binding.widthText
    qualityText = binding.qualityText
    refreshButton = binding.refreshButton
    recyclerview = binding.recyclerview
    refreshButton.setOnClickListener {
      refresh()
    }
    originalButton = binding.originalButton
    originalButton.setOnClickListener {
      setOriginalPhotos()
    }
    return binding.root
  }

  private fun setOriginalPhotos() {
    val imageData = originalImages.map {
      val imageData = process(it, it.width, 100)
      val fileSize = imageData.fileSize
      ImageData(it, fileSize)
    }
    recyclerview.adapter = MyRecyclerViewAdapter(requireContext(), imageData)
    recyclerview.invalidate()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    recyclerview.layoutManager = GridLayoutManager(requireContext(), 3)
    originalImages = arrayOf(
      resources.getDrawable(R.drawable.day_tree_a),
      resources.getDrawable(R.drawable.night_a),
      resources.getDrawable(R.drawable.daily_photo_a),
      resources.getDrawable(R.drawable.dogg),
      resources.getDrawable(R.drawable.hongkong),
      resources.getDrawable(R.drawable.random_h),
      resources.getDrawable(R.drawable.random_v),
    ).map { (it as BitmapDrawable).bitmap }
    refresh()
  }

  private fun refresh() {
    val width = (widthText.text.toString()).toInt()
    val quality = (qualityText.text.toString()).toInt()

    val resizedImages = originalImages.map {
      process(it, width, quality)
    }
    recyclerview.adapter = MyRecyclerViewAdapter(requireContext(), resizedImages)
    recyclerview.invalidate()
  }

  private fun process(bitmap: Bitmap, newWidth: Int, quality: Int): ImageData {
    val drawable = resize(bitmap, newWidth)
    val jpg = compressToJPG((drawable as BitmapDrawable).bitmap, quality)
    val resultBitmap = decompressJPG(jpg)
    return ImageData(resultBitmap, jpg.count())
  }

  private fun resize(b: Bitmap, newWidth: Int): Drawable {
    val newHeight = 1f / widthToHeightRatio * newWidth
    val bitmapResized = ThumbnailUtils.extractThumbnail(b, newWidth, newHeight.toInt())

    // val newHeight = (b.height.toFloat()) / b.width.toFloat() * newWidth.toFloat()
    // val bitmapResized = Bitmap.createScaledBitmap(b, newWidth, newHeight.toInt(), false)
    return BitmapDrawable(resources, bitmapResized)
  }

  private fun compressToJPG(bitmap: Bitmap, quality: Int): ByteArray {
    val out = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
    return out.toByteArray()
  }

  private fun decompressJPG(data: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(data, 0, data.size)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}

data class ImageData(val bitmap: Bitmap, val fileSize: Int)

