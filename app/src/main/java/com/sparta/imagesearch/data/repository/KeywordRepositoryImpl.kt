package com.sparta.imagesearch.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sparta.imagesearch.domain.repositoryInterface.KeywordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KeywordRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): KeywordRepository {
    private val KEYWORD = stringPreferencesKey("keyword")

    override suspend fun getKeyword(): Flow<String> =
        dataStore.data.map { it[KEYWORD] ?: "" }

    override suspend fun setKeyword(keyword: String){
        dataStore.edit {
            it[KEYWORD] = keyword
        }
    }
}
