package com.example.demo.service

import com.example.demo.model.Album
import com.example.demo.model.Photo
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AlbumService {

    private val restTemplate = RestTemplate()
    private val albumUrl = "https://jsonplaceholder.typicode.com/albums"
    private val photoUrl = "https://jsonplaceholder.typicode.com/photos?albumId="

   fun getAllAlbums(): List<Album> {
       val response = restTemplate.getForEntity(albumUrl, Array<Album>::class.java)
       return response.body?.toList() ?: emptyList()
   }

   fun getAlbumDetails(id: Int): Album {
       val response = restTemplate.getForEntity("$albumUrl/$id", Album::class.java)
       val album = response.body ?: throw RuntimeException("Album not found")
       val photos = restTemplate.getForEntity("$photoUrl$id", Array<Photo>::class.java).body?.toList() ?: emptyList()
       album.photos = photos
       return album
   }
}