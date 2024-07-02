package com.example.demo.model

class Album (
    val id: Int,
    val title: String,
    var photos: List<Photo> = emptyList(),
    )