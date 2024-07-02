package com.example.demo.service

import com.example.demo.model.Album
import com.example.demo.model.Photo
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class AlbumServiceTest {

    @Autowired
    private lateinit var albumService: AlbumService

    @MockBean
    private lateinit var restTemplate: RestTemplate

    @Test
    fun `should return list of albums`() {
        val albums = arrayOf(
            Album(1, "Ticket to ride"),
            Album(2, "Let it be")
        )
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums", Array<Album>::class.java))
            .thenReturn(ResponseEntity(albums, HttpStatus.OK))

        val result = albumService.getAllAlbums()
        assertEquals(2, result.size)
        assertEquals("Ticket to ride", result[0].title)
        assertEquals("Let it be", result[1].title)
    }

    @Test
    fun `should return album details`() {
        val album = Album(1, "I'll follow the sun")
        val photos = arrayOf(
            Photo(1, "Photo 1", "url1", "thumbnailUrl1"),
            Photo(2, "Photo 2", "url2", "thumbnailUrl2")
        )
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums/1", Album::class.java))
            .thenReturn(ResponseEntity(album, HttpStatus.OK))
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos?albumId=1", Array<Photo>::class.java))
            .thenReturn(ResponseEntity(photos, HttpStatus.OK))

        val result = albumService.getAlbumDetails(1)
        assertEquals(1, result.id)
        assertEquals("I'll follow the sun", result.title)
        assertEquals(2, result.photos.size)
        assertEquals("Photo 1", result.photos[0].title)
        assertEquals("Photo 2", result.photos[1].title)
    }
}