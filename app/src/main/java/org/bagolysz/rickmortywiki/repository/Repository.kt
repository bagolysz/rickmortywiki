package org.bagolysz.rickmortywiki.repository

import io.reactivex.Observable

interface Repository<T> {

    fun getListData(): Observable<List<T>>

    fun getItemById(id: Int): Observable<T>

    fun refresh()
}