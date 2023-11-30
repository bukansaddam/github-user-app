package com.saddam.mygithub.ui.detail.tabLayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saddam.mygithub.data.remote.response.DetailsItem
import com.saddam.mygithub.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private val followerViewModel by viewModels<FollowersViewModel>()

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollower.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollower.addItemDecoration(itemDecoration)

        val username = arguments?.getString(EXTRA_USERNAME)

        followerViewModel.getFollower(username.toString())

        followerViewModel.users.observe(viewLifecycleOwner) { user ->
            setFollowerData(user)
        }

        followerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setFollowerData(data: List<DetailsItem>) {
        val adapter = FollowsAdapter()
        adapter.submitList(data)
        binding.rvFollower.adapter = adapter

        adapter.setOnClickCallback(object : FollowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DetailsItem) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}