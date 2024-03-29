package com.dunn.playwithandroid.base.net.exception

/**
 * Created by DunnLin on 2019/7/24.
 */
class ResponseException : Exception {

    private var errorCode: String
    private var errorMessage: String?

    constructor(throwable: Throwable, errorCode: Int, errorMessage: String?) : super(throwable) {
        this.errorCode = errorCode.toString()
        this.errorMessage = errorMessage
    }

    constructor(throwable: Throwable, errorCode: String, errorMessage: String?) : super(throwable) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

    fun getErrorCode(): String {
        return errorCode
    }

    fun setErrorCode(errorCode: String) {
        this.errorCode = errorCode
    }

    fun getErrorMessage(): String? {
        return errorMessage
    }

    fun setErrorMessage(errorMessage: String) {
        this.errorMessage = errorMessage
    }

    override fun toString(): String {
        return "[$errorCode, $errorMessage]"
    }

}