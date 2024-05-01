package com.sparta.imagesearch.data

sealed class ApiResponse<out T>{
    object Loading: ApiResponse<Nothing>()
    data class Success<T>(val data: T): ApiResponse<T>()
    sealed class Fail: ApiResponse<Nothing>() {
        data class Error(val code: Int, val message: String?) : Fail()
        data class Exception(val e: Throwable) : Fail()
    }
}