package com.saddam.mygithub.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.saddam.mygithub.data.local.entity.Users
import com.saddam.mygithub.databinding.ActivityFavoriteBinding
import com.saddam.mygithub.helper.ViewModelFactory
import com.saddam.mygithub.ui.detail.DetailUser
import com.saddam.mygithub.ui.setting.SettingPreferences
import com.saddam.mygithub.ui.setting.dataStore

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configToolbar()

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllUser().observe(this){userList ->
            if (userList != null){
                setUserData(userList)
            }
        }

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
    }

    private fun configToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Favorites")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun setUserData(usersBody: List<Users>?) {
        adapter = FavoritesAdapter()
        adapter.submitList(usersBody)
        binding.rvFavorite.adapter = adapter

        adapter.setOnClickCallback(object : FavoritesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: Users) {
        val intent = Intent(this@FavoriteActivity, DetailUser::class.java)
        intent.putExtra(DetailUser.EXTRA_USER, user.username)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}