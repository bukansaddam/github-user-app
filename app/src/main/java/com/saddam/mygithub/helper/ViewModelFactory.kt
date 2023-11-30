package com.saddam.mygithub.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saddam.mygithub.ui.detail.DetailViewModel
import com.saddam.mygithub.ui.favorite.FavoriteViewModel
import com.saddam.mygithub.ui.main.MainViewModel
import com.saddam.mygithub.ui.setting.SettingPreferences
import com.saddam.mygithub.ui.setting.SettingViewModel

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val pref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, pref: SettingPreferences): ViewModelFactory{
            if (instance == null){
                synchronized(ViewModelFactory::class.java){
                    instance = ViewModelFactory(application, pref)
                }
            }
            return instance as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(pref) as T
        }else if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class: ${modelClass.name}")
    }
}