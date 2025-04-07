package com.example.pix.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pix.R
import com.example.pix.databinding.FragmentImageBinding
import kotlinx.serialization.json.Json

class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentImage is null")
    private val viewModel: ImageViewModel by activityViewModels()
    private val imageArgs: ImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val pictureJson = imageArgs.picture
        viewModel.loadPicture(Json.decodeFromString(pictureJson))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel.currentImage.observe(viewLifecycleOwner) { currentPicture ->
                if (currentPicture.picture != null) {
                    Glide.with(ivPicture.context)
                        .load(currentPicture.picture.url)
                        .error(R.drawable.img_error)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivPicture)
                    tvTitle.text = currentPicture.picture.title
                }
            }
            btnClose.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}