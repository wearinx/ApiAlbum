package com.example.demo.service

import com.example.demo.model.Album
import com.example.demo.model.Photo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@SpringBootTest
class AlbumServiceTest {

    @Autowired
    private lateinit var albumService: AlbumService

    @MockBean
    private lateinit var restTemplate: RestTemplate

    @Test
    fun `should return list of albums with photos`() {
        val albums = arrayOf(
            Album(1, "quidem molestiae enim"),
            Album(2, "reprehenderit est deserunt")
        )
        val photosAlbum1 = arrayOf(
            Photo(1, "accusamus beatae ad facilis cum similique qui sunt", "https://via.placeholder.com/600/92c952", "https://via.placeholder.com/150/92c952"),
            Photo(2, "reprehenderit est deserunt velit ipsam", "https://via.placeholder.com/600/771796", "https://via.placeholder.com/150/771796")
        )
        val photosAlbum2 = arrayOf(
            Photo(3, "officia porro iure quia iusto qui ipsa ut modi", "https://via.placeholder.com/600/24f355", "https://via.placeholder.com/150/24f355"),
            Photo(4, "culpa odio esse rerum omnis laboriosam voluptate repudiandae", "https://via.placeholder.com/600/d32776", "https://via.placeholder.com/150/d32776")
        )

        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums", Array<Album>::class.java))
            .thenReturn(ResponseEntity(albums, HttpStatus.OK))
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos?albumId=1", Array<Photo>::class.java))
            .thenReturn(ResponseEntity(photosAlbum1, HttpStatus.OK))
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos?albumId=2", Array<Photo>::class.java))
            .thenReturn(ResponseEntity(photosAlbum2, HttpStatus.OK))

        val result = albumService.getAllAlbums()
        assertEquals(100, result.size)
        assertEquals("quidem molestiae enim", result[0].title)
        assertEquals(50, result[0].photos.size)
        assertEquals("accusamus beatae ad facilis cum similique qui sunt", result[0].photos[0].title)
        assertEquals("reprehenderit est deserunt velit ipsam", result[0].photos[1].title)

        assertEquals("sunt qui excepturi placeat culpa", result[1].title)
        assertEquals(50, result[1].photos.size)
        assertEquals("non sunt voluptatem placeat consequuntur rem incidunt", result[1].photos[0].title)
        assertEquals("eveniet pariatur quia nobis reiciendis laboriosam ea", result[1].photos[1].title)
    }

    @Test
    fun `should return album details`() {
        val album = Album(1, "quidem molestiae enim")
        val photos = arrayOf(
            Photo(1, "accusamus beatae ad facilis cum similique qui sunt", "https://via.placeholder.com/600/92c952", "https://via.placeholder.com/150/92c952"),
            Photo(50, "reprehenderit est deserunt velit ipsam", "https://via.placeholder.com/600/771796", "https://via.placeholder.com/150/771796")
        )

        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums/1", Album::class.java))
            .thenReturn(ResponseEntity(album, HttpStatus.OK))
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos?albumId=1", Array<Photo>::class.java))
            .thenReturn(ResponseEntity(photos, HttpStatus.OK))

        val result = albumService.getAlbumDetails(1)
        assertEquals(1, result.id)
        assertEquals("quidem molestiae enim", result.title)
        assertEquals(50, result.photos.size)
        assertEquals("accusamus beatae ad facilis cum similique qui sunt", result.photos[0].title)
        assertEquals("reprehenderit est deserunt velit ipsam", result.photos[1].title)
    }

    @Test
    fun `should throw exception when album not found`() {
        Mockito.`when`(restTemplate.getForEntity("https://jsonplaceholder.typicode.com/albums/999", Album::class.java))
            .thenThrow(HttpClientErrorException(HttpStatus.NOT_FOUND))

        val exception = assertThrows<RuntimeException> {
            albumService.getAlbumDetails(999)
        }
        assertEquals("404 Not Found: \"{}\"", exception.message)
    }
}
