package com.example.demo.model

class Album (
    val id: Int,
    var title: String,
    var photos: List<Photo> = emptyList(),
    )