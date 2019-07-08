package org.bagolysz.rickmortywiki.repository.network

import io.reactivex.Observable
import org.bagolysz.rickmortywiki.model.data.Character

class CharacterNetworkRepository(private val networkApi: NetworkApi) : NetworkRepository<Character>() {

    override fun getListData(): Observable<List<Character>> {
        val pageNumber = nextPageNumber
        val response = if (pageNumber == null) {
            networkApi.getCharacterList()
        } else {
            networkApi.getCharacterList(pageNumber)
        }

        return response.map {
            nextPageNumber = parseNextPageNumber(it.info)
            it.results
        }
    }

    override fun getItemById(id: Int): Observable<Character> {
        return networkApi.getCharacterById(id)
    }
}