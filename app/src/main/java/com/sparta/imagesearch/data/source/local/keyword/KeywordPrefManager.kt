package com.sparta.imagesearch.data.source.local.keyword

import com.sparta.imagesearch.MyApplication

object KeywordPrefManager {
    private const val KEYWORD_PREF_NAME = "keyword_shared_preferences"
    private const val KEYWORD_PREF_KEY = "keyword_pref_key"

    fun loadKeyword(): String{
        val pref = MyApplication.appContext!!.getSharedPreferences(KEYWORD_PREF_NAME, 0)
        return pref.getString(KEYWORD_PREF_KEY, "") ?: ""
    }

    fun saveKeyword(keyWord:String){
        val pref = MyApplication.appContext!!.getSharedPreferences(KEYWORD_PREF_NAME, 0)
        pref.edit().putString(KEYWORD_PREF_KEY, keyWord).commit()
    }
}