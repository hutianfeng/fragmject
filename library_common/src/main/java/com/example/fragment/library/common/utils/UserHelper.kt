package com.example.fragment.library.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fragment.library.base.db.SimpleDBHelper
import com.example.fragment.library.common.bean.CoinBean
import com.example.fragment.library.common.bean.UserBean
import com.google.gson.Gson

object UserHelper {

    private const val USER = "user"
    private const val COIN = "coin"

    val userUpdate = MutableLiveData<Boolean>()

    fun setUser(userBean: UserBean) {
        SimpleDBHelper.set(USER, userBean.toJson())
        userUpdate.postValue(true)
    }

    fun getUser(): LiveData<UserBean> {
        return Transformations.map(SimpleDBHelper.get(USER)) {
            try {
                Gson().fromJson(it.toString(), UserBean::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                UserBean::class.java.newInstance()
            }
        }
    }

    fun setCoin(coinBean: CoinBean) {
        SimpleDBHelper.set(COIN, coinBean.toJson())
    }

    fun getCoin(): LiveData<CoinBean> {
        return Transformations.map(SimpleDBHelper.get(COIN)) {
            try {
                Gson().fromJson(it.toString(), CoinBean::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                CoinBean::class.java.newInstance()
            }
        }
    }

}