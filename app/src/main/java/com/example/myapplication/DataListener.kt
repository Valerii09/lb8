package com.example.myapplication

interface DataListener {
    fun getData(data: Boolean)
    fun sendData(): Pair<Int, Int>
}