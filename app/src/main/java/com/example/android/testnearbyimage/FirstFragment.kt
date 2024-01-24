package com.example.android.testnearbyimage

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.testnearbyimage.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

  private var _binding: FragmentFirstBinding? = null
  private val binding get() = _binding!!

  val compressQuality = 80
  val width = 100

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    _binding = FragmentFirstBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val data =
      arrayOf(resources.getDrawable(R.drawable.day_tree_a),
              resources.getDrawable(R.drawable.night_a),
              resources.getDrawable(R.drawable.daily_photo_a))

    val images = data.map { (it as BitmapDrawable).bitmap }

    _binding!!.recyclerview.adapter = MyRecyclerViewAdapter(requireContext(), images)
    _binding!!.recyclerview.layoutManager = GridLayoutManager(requireContext(), 3)
  }

  private fun resize(image: Drawable, newWidth: Int): Drawable {
    val b = (image as BitmapDrawable).bitmap
    val newHeight = b.height / b.width * newWidth
    val bitmapResized = Bitmap.createScaledBitmap(b, newWidth, newHeight, false)
    return BitmapDrawable(resources, bitmapResized)
  }

  private fun compressImage() {
    
  }




  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}

