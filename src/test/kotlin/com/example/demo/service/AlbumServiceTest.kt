package com.example.demo.service

import com.example.demo.model.Album
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class AlbumServiceTest {

    private val restTemplate = Mockito.mock(RestTemplate::class.java)
    private val albumService = AlbumService()

    @Test
    fun `should return list of albums`() {
        val albums = listOf(Album(1, "Tiket to ride"), Album(2, "Let it be"))
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums", Array<Album>::class.java))
                .thenReturn(ResponseEntity(albums.toTypedArray(), HttpStatus.OK))

        val result = albumService.getAllAlbums()

        assertEquals(2, result.size)
    }

    @Test
    fun `should return album details`() {
        val album = Album(1, "Tiket to ride")
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums/1", Album::class.java))
                .thenReturn(ResponseEntity(album, HttpStatus.OK))

        val result = albumService.getAlbumDetails(1)

        assertNotNull(result)
        assertEquals(1, result.id.toInt())
    }
}