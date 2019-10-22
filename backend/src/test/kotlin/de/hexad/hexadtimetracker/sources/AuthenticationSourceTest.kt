package de.hexad.hexadtimetracker.sources

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.web.reactive.function.client.WebClient

class AuthenticationSourceTest {

    lateinit var authenticationSource: AuthenticationSource

    val mockServer = MockWebServer()

    @Before
    fun setUp() {
        authenticationSource = AuthenticationSource(WebClient.create(mockServer.url("/").toString()))
    }

    @After
    fun afterTests() {
        mockServer.shutdown()
    }

    @Test
    fun `should return valid token`() {
        //given
        val mockedResponse = MockResponse()
        mockedResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("#Tue Nov 30 02:16:57 PST 2010\nAUTHTOKEN=e07119171812c29b3a0dacdb79a57e3f\nRESULT=TRUE")
        mockServer.enqueue(mockedResponse)

        //when
        val token = authenticationSource.getAuthenticationToken("valid_email@hexad.de", "valid_password")

        //then
        Assert.assertEquals("e07119171812c29b3a0dacdb79a57e3f", token)
    }

    @Test
    fun `should throw AuthenticationServiceException if response's result is equal false`() {
        //given
        val mockedResponse = MockResponse()
        mockedResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("#Fri Oct 11 03:04:25 PDT 2019\nCAUSE=NO_SUCH_USER\nRESULT=FALSE")
        mockServer.enqueue(mockedResponse)

        //when - then
        Assertions.assertThrows(AuthenticationServiceException::class.java) { authenticationSource.getAuthenticationToken("invalid_email@hexad.de", "invalid_password") }
    }


}