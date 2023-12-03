package dev.awd.injaaz.data


sealed class Result {

    class Success<T>(val data: T) : Result()
    class Failure(val error: String) : Result()
}