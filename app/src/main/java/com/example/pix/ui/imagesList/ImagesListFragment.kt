package com.example.pix.ui.imagesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pix.databinding.FragmentListImagesBinding
import com.example.pix.domain.entity.Picture
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ImagesListFragment : Fragment() {
    private var _binding: FragmentListImagesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentListImages is null")
    private val viewModel: ImagesListViewModel by activityViewModels()
    private val imagesAdapter: ImagesListAdapter = ImagesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListImagesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val picturesList = viewModel.currentList.value?.pictures
        if (picturesList != null)
            initRecycler(picturesList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(picturesList: List<Picture>) {
        viewModel.currentList.observe(viewLifecycleOwner) { currentState ->
            imagesAdapter.setDataset(currentState.pictures)
            if (currentState.pictures.isNotEmpty())
                imagesAdapter.notifyItemRangeChanged(0, currentState.pictures.size)
        }
        with(binding) {
            rvImagesList.adapter = imagesAdapter
            imagesAdapter.setOnItemClickListener(object : ImagesListAdapter.OnItemClickListener {
                override fun onItemClick(picture: Picture) {
                    openPicture(picture)
                }
            })
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    imagesAdapter.setDataset(listOf())
                    if (query.isNullOrEmpty())
                        viewModel.loadRecentPictures(listOf())
                    else
                        viewModel.searchPictures(query)

                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
        viewModel.loadRecentPictures(picturesList)
    }

    private fun openPicture(picture: Picture) {
        findNavController().navigate(
            ImagesListFragmentDirections.actionImagesListFragmentToImageFragment(
                Json.encodeToString(picture)
            )
        )
    }
}