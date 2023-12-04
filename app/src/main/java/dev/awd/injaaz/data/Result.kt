package dev.awd.injaaz.data


sealed class Result<T>(
    val data: T? = null,
    val error: String? = null
) {

    class Success<T>(data: T) : Result<T>(data = data)
    class Failure<T>(error: String) : Result<T>(error = error)
}