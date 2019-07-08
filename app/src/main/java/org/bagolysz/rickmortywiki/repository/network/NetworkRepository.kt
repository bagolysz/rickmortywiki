package org.bagolysz.rickmortywiki.repository.network

import android.util.Log
import org.bagolysz.rickmortywiki.model.data.Info
import org.bagolysz.rickmortywiki.repository.Repository

abstract class NetworkRepository<T> : Repository<T> {

    protected var nextPageNumber: Int? = null

    protected fun parseNextPageNumber(responseInfo: Info): Int? {
        var pageNumber: Int? = null
        val strings = responseInfo.next.split("=")
        if (strings.size == 2) {
            try {
                pageNumber = strings[1].toInt()
            } catch (e: NumberFormatException) {
                Log.e(TAG, e.toString(), e)
            }
        }
        return pageNumber
    }

    override fun refresh() {
        nextPageNumber = null
    }

    companion object {
        private const val TAG = "RMRepo"
    }
}