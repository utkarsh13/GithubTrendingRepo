package com.example.gojekassignment.utils

fun isCacheGood(startTime: Long, endTime: Long): Boolean {
    return (endTime - startTime) < (60*60*2*1000)
}