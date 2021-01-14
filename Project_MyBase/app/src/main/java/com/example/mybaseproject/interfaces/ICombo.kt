package com.example.mybaseproject.interfaces

interface ICombo {
    @Throws(Exception::class)
    fun actionItem(item: Any, type: Int)
}