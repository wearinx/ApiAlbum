package com.example.demo.controller

import com.example.demo.model.Album
import com.example.demo.service.AlbumService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/albums")
@Tag(name = "Album", description = "API para gestionar álbumes de fotos")
class AlbumController (private val albumService: AlbumService){

    @GetMapping
    @Operation(summary = "Obtener todos los álbumes", description = "Devuelve una lista de todos los álbumes")
    fun getAllAlbums(): List<Album> {
        return albumService.getAllAlbums();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles del álbum", description = "Devuelve los detalles de un álbum especificado por ID")
    fun getAlbumDetails(@PathVariable id: Int): Album {
        return albumService.getAlbumDetails(id);
    }

}