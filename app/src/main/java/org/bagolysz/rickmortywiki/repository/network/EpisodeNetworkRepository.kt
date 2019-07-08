package org.bagolysz.rickmortywiki.repository.network

import io.reactivex.Observable
import org.bagolysz.rickmortywiki.model.data.Episode

class EpisodeNetworkRepository(private val networkApi: NetworkApi) : NetworkRepository<Episode>() {

    override fun getListData(): Observable<List<Episode>> {
        val pageNumber = nextPageNumber
        val response = if (pageNumber == null) {
            networkApi.getEpisodeList()
        } else {
            networkApi.getEpisodeList(pageNumber)
        }

        return response.map {
            nextPageNumber = parseNextPageNumber(it.info)
            it.results
        }
    }

    override fun getItemById(id: Int): Observable<Episode> {
        return networkApi.getEpisodeById(id)
    }
}