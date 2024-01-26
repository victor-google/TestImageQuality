package com.example.android.testnearbyimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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

  val originalImages = arrayOf(
    resources.getDrawable(R.drawable.day_tree_a),
    resources.getDrawable(R.drawable.night_a),
    resources.getDrawable(R.drawable.daily_photo_a)
  ).map { (it as BitmapDrawable).bitmap }

  val compressQuality = 80
  val width = 100

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    _binding = FragmentFirstBinding.inflate(inflater, container, false)
    widthText = binding.widthText
    qualityText = binding.qualityText
    refreshButton = binding.refreshButton
    refreshButton.setOnClickListener {
      refresh()
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // _binding!!.recyclerview.adapter = MyRecyclerViewAdapter(requireContext(), originalImages)
    // _binding!!.recyclerview.layoutManager = GridLayoutManager(requireContext(), 3)
  }

  private fun refresh() {
    val width = widthText.text as Int
    val quality = qualityText.text as Int

    val resizedImages = originalImages.map {
      process(it, width, quality)
    }
    _binding!!.recyclerview.adapter = MyRecyclerViewAdapter(requireContext(), resizedImages)
    _binding!!.recyclerview.invalidate()
  }

  private fun process(bitmap: Bitmap, newWidth: Int, quality: Int): Bitmap {
    val drawable = resize(bitmap, newWidth)
    val jpg = compressToJPG((drawable as BitmapDrawable).bitmap, quality)
    val resultBitmap = decompressJPG(jpg)
    return resultBitmap
  }

  private fun resize(b: Bitmap, newWidth: Int): Drawable {
    // val b = (image as BitmapDrawable).bitmap
    val newHeight = b.height / b.width * newWidth
    val bitmapResized = Bitmap.createScaledBitmap(b, newWidth, newHeight, false)
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

