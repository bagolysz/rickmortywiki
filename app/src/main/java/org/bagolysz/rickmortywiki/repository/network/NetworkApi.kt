package org.bagolysz.rickmortywiki.repository.network

import io.reactivex.Observable
import org.bagolysz.rickmortywiki.model.data.Character
import org.bagolysz.rickmortywiki.model.data.Episode
import org.bagolysz.rickmortywiki.model.data.Location
import org.bagolysz.rickmortywiki.model.response.ItemListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {

    @GET("character/")
    fun getCharacterList(): Observable<ItemListResponse<Character>>

    @GET("character/")
    fun getCharacterList(@Query("page") pageNumber: Int): Observable<ItemListResponse<Character>>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int): Observable<Character>


    @GET("location/")
    fun getLocationList(): Observable<ItemListResponse<Location>>

    @GET("location/")
    fun getLocationList(@Query("page") pageNumber: Int): Observable<ItemListResponse<Location>>

    @GET("location/{id}")
    fun getLocationById(@Path("id") id: Int): Observable<Location>


    @GET("episode/")
    fun getEpisodeList(): Observable<ItemListResponse<Episode>>

    @GET("episode/")
    fun getEpisodeList(@Query("page") pageNumber: Int): Observable<ItemListResponse<Episode>>

    @GET("episode/{id}")
    fun getEpisodeById(@Path("id") id: Int): Observable<Episode>

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}