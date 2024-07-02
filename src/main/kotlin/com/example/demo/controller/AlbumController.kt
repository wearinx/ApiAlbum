package com.example.demo.controller

import com.example.demo.model.Album
import com.example.demo.service.AlbumService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/album")
class AlbumController (private val albumService: AlbumService){

    @GetMapping
    fun getAllAlbums(): List<Album> {
        return albumService.getAllAlbums();
    }

    @GetMapping("/{id}")
    fun getAlbumDetails(@PathVariable id: Int): Album {
        return albumService.getAlbumDetails(id);
    }

}