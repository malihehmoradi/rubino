package com.example.rubinoapp.ui.postdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.rubinoapp.R
import com.example.rubinoapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var postId: Int = 0
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    private val adapter: MessageAdapter = MessageAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        postId = arguments?.getInt("postId")!!

        binding.recyclerView.adapter = adapter

        viewModel.getSelectedPost(postId)
        lifecycleScope.launch {
            viewModel.selectedPost.collectLatest { it ->
                binding.post = it
                binding.imagePost.load(it?.image)
                if (it?.isLiked == true) binding.btnLikes.setImageResource(R.drawable.round_favorite_24) else binding.btnLikes.setImageResource(
                    R.drawable.round_favorite_border_24
                )

                if (viewModel.comments.value.isEmpty()) {
                    viewModel.getTenMoreComment()
                }
            }
        }


        lifecycleScope.launch {
            viewModel.comments.collectLatest { list ->
                adapter.list = list
                adapter.notifyDataSetChanged()
            }
        }
        binding.btnLoadComments.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getTenMoreComment()
            }
        }

        binding.btnLikes.setOnClickListener {
            if (viewModel.selectedPost.value?.isLiked == true) {
                binding.btnLikes.setImageResource(R.drawable.round_favorite_border_24)
                viewModel.disLikePost(viewModel.selectedPost.value!!.id)
            } else {
                viewModel.likePost(viewModel.selectedPost.value!!.id)
                binding.btnLikes.setImageResource(R.drawable.round_favorite_24)
            }
        }
    }

}