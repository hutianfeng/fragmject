package com.example.fragment.project.utils

import android.util.Log
import com.example.fragment.project.bean.UserBean
import com.example.miaow.base.db.KVDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 数据持久化辅助类
 */
object WanHelper {

    private const val SEARCH_HISTORY = "search_history"
    private const val WEB_BOOKMARK = "web_bookmark"
    private const val WEB_HISTORY = "web_history"
    private const val UI_MODE = "ui_mode"
    private const val USER = "user"

    /**
     * 设置搜索历史
     */
    fun setSearchHistory(list: List<String>) {
        KVDatabase.set(SEARCH_HISTORY, Gson().toJson(list))
    }

    /**
     * 获取搜索历史
     */
    suspend fun getSearchHistory(): List<String> {
        return try {
            val json = KVDatabase.get(SEARCH_HISTORY)
            Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: ArrayList()
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message.toString())
            ArrayList()
        }
    }

    fun setWebBookmark(list: List<String>) {
        KVDatabase.set(WEB_BOOKMARK, Gson().toJson(list))
    }

    suspend fun getWebBookmark(): List<String> {
        return try {
            val json = KVDatabase.get(WEB_BOOKMARK)
            Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: ArrayList()
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message.toString())
            ArrayList()
        }
    }

    fun setWebHistory(list: List<String>) {
        KVDatabase.set(WEB_HISTORY, Gson().toJson(list))
    }

    suspend fun getWebHistory(): List<String> {
        return try {
            val json = KVDatabase.get(WEB_HISTORY)
            Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: ArrayList()
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message.toString())
            ArrayList()
        }
    }

    /**
     * mode :
     *      -1 : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
     *       1 : AppCompatDelegate.MODE_NIGHT_NO,
     *       2 : AppCompatDelegate.MODE_NIGHT_YES
     */
    fun setUiMode(mode: String) {
        KVDatabase.set(UI_MODE, mode)
    }

    /**
     * 显示模式状态
     * return
     *      -1 : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
     *       1 : AppCompatDelegate.MODE_NIGHT_NO,
     *       2 : AppCompatDelegate.MODE_NIGHT_YES
     */
    fun getUiMode(result: (String) -> Unit) {
        KVDatabase.get(UI_MODE) {
            result.invoke(it)
        }
    }

    /**
     * 设置用户信息
     */
    fun setUser(userBean: UserBean?) {
        userBean?.let {
            KVDatabase.set(USER, it.toJson())
        }
    }

    /**
     * 获取用户信息
     */
    fun getUser(result: (UserBean) -> Unit) {
        KVDatabase.get(USER) {
            val userBean = try {
                Gson().fromJson(it, UserBean::class.java) ?: UserBean()
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.message.toString())
                UserBean()
            }
            result.invoke(userBean)
        }
    }

    /**
     * 关闭数据库
     */
    fun close() {
        KVDatabase.closeDatabase()
    }

}