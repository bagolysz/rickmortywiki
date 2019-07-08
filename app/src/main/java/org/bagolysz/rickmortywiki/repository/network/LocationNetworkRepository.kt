package org.bagolysz.rickmortywiki.repository.network

import io.reactivex.Observable
import org.bagolysz.rickmortywiki.model.data.Location

class LocationNetworkRepository(private val networkApi: NetworkApi) :
    NetworkRepository<Location>() {

    override fun getListData(): Observable<List<Location>> {
        val pageNumber = nextPageNumber
        val response = if (pageNumber == null) {
            networkApi.getLocationList()
        } else {
            networkApi.getLocationList(pageNumber)
        }

        return response.map {
            nextPageNumber = parseNextPageNumber(it.info)
            it.results
        }
    }

    override fun getItemById(id: Int): Observable<Location> {
        return networkApi.getLocationById(id)
    }
}