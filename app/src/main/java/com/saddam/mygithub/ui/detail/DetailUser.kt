package com.saddam.mygithub.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saddam.mygithub.R
import com.saddam.mygithub.data.remote.response.UserDetail
import com.saddam.mygithub.databinding.ActivityDetailUserBinding
import com.saddam.mygithub.helper.ViewModelFactory
import com.saddam.mygithub.ui.setting.SettingPreferences
import com.saddam.mygithub.ui.setting.dataStore

class DetailUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding

    private lateinit var detailViewModel: DetailViewModel

    private val sectionPagerAdapter = SectionPagerAdapter(this)

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )

        const val EXTRA_USER = "extra_user"
    }

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configToolbar()
        configViewPager()

        val username = intent.getStringExtra(EXTRA_USER)

        detailViewModel = obtainViewModel(this@DetailUser)

        detailViewModel.users.observe(this) { data ->
            setUserData(data)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getUserDetail(username.toString())
        sectionPagerAdapter.username = username

        binding.fabFavorite.setOnClickListener(this)

        detailViewModel.getAllUser().observe(this) { userList ->
            if (userList != null) {
                for (user in userList) {
                    val isInFavorite = userList.any { it.username == username }
                    isFavorite = if (isInFavorite) {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                        true
                    } else {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                        false
                    }
                }
            }
        }

    }

    private fun configViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun configToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setUserData(data: UserDetail?) {
        Glide.with(binding.root.context)
            .load(data?.avatarUrl)
            .circleCrop()
            .into(binding.imgUser)
        binding.tvName.text = data?.name
        binding.tvUsername.text = data?.username
        binding.tvFollower.text = data?.followers.toString()
        binding.tvFollowing.text = data?.following.toString()

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.layoutProfil.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.layoutProfil.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_favorite -> {
                val data = detailViewModel.users.value
                if (data != null) {
                    val user = detailViewModel.setDataUser(data)
                    if (isFavorite){
                        detailViewModel.delete(user)
                        Toast.makeText(this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show()
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                    }else{
                        detailViewModel.insert(user)
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                        Toast.makeText(this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}