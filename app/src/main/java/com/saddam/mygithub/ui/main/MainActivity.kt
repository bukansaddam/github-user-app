package com.saddam.mygithub.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saddam.mygithub.R
import com.saddam.mygithub.data.remote.response.DetailsItem
import com.saddam.mygithub.databinding.ActivityMainBinding
import com.saddam.mygithub.helper.ViewModelFactory
import com.saddam.mygithub.ui.detail.DetailUser
import com.saddam.mygithub.ui.favorite.FavoriteActivity
import com.saddam.mygithub.ui.setting.SettingActivity
import com.saddam.mygithub.ui.setting.SettingPreferences
import com.saddam.mygithub.ui.setting.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = UsersAdapter()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(application, pref)
        val viewModel: MainViewModel by viewModels {
            factory
        }

        viewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.getAllUser()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.text = searchView.text
                searchView.hide()
                if (searchView.text?.isEmpty() == true) {
                    mainViewModel.getAllUser()
                } else {
                    mainViewModel.getUserBySearch(searchView.text.toString())
                }
                false
            }
        }

        mainViewModel.users.observe(this@MainActivity) { user ->
            setUserData(user)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvAllUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvAllUser.addItemDecoration(itemDecoration)
        binding.rvAllUser.adapter = adapter

        adapter.setOnClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DetailsItem) {
                showSelectedUser(data)
            }
        })

        binding.appBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId){
                R.id.menu_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }
                R.id.menu_setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }
                else -> false
            }
        }

    }

    private fun setUserData(result: List<DetailsItem>){
        val adapter = UsersAdapter()
        adapter.submitList(result)
        binding.rvAllUser.adapter = adapter

        adapter.setOnClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DetailsItem) {
                showSelectedUser(data)
            }

        })
    }

    private fun showSelectedUser(user: DetailsItem) {
        val intent = Intent(this@MainActivity, DetailUser::class.java)
        intent.putExtra(DetailUser.EXTRA_USER, user.username)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}

