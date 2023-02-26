package com.example.rubinoapp.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.rubinoapp.R
import com.example.rubinoapp.data.database.Post
import com.example.rubinoapp.databinding.FragmentPostBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private val viewModel: PostViewModel by viewModels()
    private val adapter = PostAdapter(
        clickCallback = { post ->
            val bundle = bundleOf("postId" to post.id)
            findNavController().navigate(R.id.action_postFragment_to_detailFragment, bundle)
        },
        likeCallback = { post ->
            if (post.isLiked) {
                viewModel.disLikePost(post.id)
            } else {
                viewModel.likePost(post.id)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        /**
         * Check if the fake posts is not saved in the database at first save them
         */
        lifecycleScope.launch {
            viewModel.postsCount.collectLatest { count ->
                if (count == 0) {
                    val jsonString = context?.assets?.open("fakePosts.json")?.bufferedReader().use {
                        it?.readText()
                    }
                    val list: List<Post> = Gson().fromJson(
                        jsonString,
                        object : TypeToken<List<Post>>() {}.type
                    ) as List<Post>
                    viewModel.saveFakePosts(list)
                }
            }
        }


        lifecycleScope.launch {
            viewModel.items.collectLatest {
                adapter.submitData(it)
            }
        }


        // Use the CombinedLoadStates provided by the loadStateFlow on the ArticleAdapter to
        // show progress bars when more data is being fetched
        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
            adapter.loadStateFlow.collect {
                binding.appendProgress.isVisible = it.source.append is LoadState.Loading
            }
//            }
        }

    }

}