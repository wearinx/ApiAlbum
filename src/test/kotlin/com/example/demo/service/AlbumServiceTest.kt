package com.example.demo.service

import com.example.demo.model.Album
import com.example.demo.model.Photo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals

@SpringBootTest
class AlbumServiceTest {

    @Autowired
    private lateinit var albumService: AlbumService

    @MockBean
    private lateinit var restTemplate: RestTemplate

    @Test
    fun `should return list of albums with photos`() {
        val albums = arrayOf(
            Album(1, "Album 1"),
            Album(2, "Album 2")
        )
        val photosAlbum1 = arrayOf(
            Photo(1, "Photo 1", "url1", "thumbnailUrl1"),
            Photo(2, "Photo 2", "url2", "thumbnailUrl2")
        )
        val photosAlbum2 = arrayOf(
            Photo(3, "Photo 3", "url3", "thumbnailUrl3"),
            Photo(4, "Photo 4", "url4", "thumbnailUrl4")
        )

        `when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums", Array<Album>::class.java))
            .thenReturn(ResponseEntity(albums, HttpStatus.OK))
        `when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos?albumId=1", Array<Photo>::class.java))
            .thenReturn(ResponseEntity(photosAlbum1, HttpStatus.OK))
        `when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos?albumId=2", Array<Photo>::class.java))
            .thenReturn(ResponseEntity(photosAlbum2, HttpStatus.OK))

        val result = albumService.getAllAlbums()
        assertEquals(2, result.size)
        assertEquals("Album 1", result[0].title)
        assertEquals(2, result[0].photos.size)
        assertEquals("Photo 1", result[0].photos[0].title)
        assertEquals("Photo 2", result[0].photos[1].title)

        assertEquals("Album 2", result[1].title)
        assertEquals(2, result[1].photos.size)
        assertEquals("Photo 3", result[1].photos[0].title)
        assertEquals("Photo 4", result[1].photos[1].title)
    }

    @Test
    fun `should return album details`() {
        val album = Album(1, "Album 1")
        val photos = arrayOf(
            Photo(1, "Photo 1", "url1", "thumbnailUrl1"),
            Photo(2, "Photo 2", "url2", "thumbnailUrl2")
        )

        `when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums/1", Album::class.java))
            .thenReturn(ResponseEntity(album, HttpStatus.OK))
        `when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos?albumId=1", Array<Photo>::class.java))
            .thenReturn(ResponseEntity(photos, HttpStatus.OK))

        val result = albumService.getAlbumDetails(1)
        assertEquals(1, result.id)
        assertEquals("Album 1", result.title)
        assertEquals(2, result.photos.size)
        assertEquals("Photo 1", result.photos[0].title)
        assertEquals("Photo 2", result.photos[1].title)
    }

    @Test
    fun `should throw exception when album not found`() {
        `when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums/999", Album::class.java))
            .thenThrow(HttpClientErrorException(HttpStatus.NOT_FOUND))

        val exception = assertThrows<RuntimeException> {
            albumService.getAlbumDetails(999)
        }
        assertEquals("Album not found", exception.message)
    }

    @Test
    fun `should throw exception on error fetching album details`() {
        `when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums/1", Album::class.java))
            .thenThrow(HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE))

        val exception = assertThrows<RuntimeException> {
            albumService.getAlbumDetails(1)
        }
        assertEquals("Error fetching album details: 503 SERVICE_UNAVAILABLE", exception.message)
    }
}
