package org.bagolysz.rickmortywiki.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.bagolysz.rickmortywiki.repository.Repository
import org.bagolysz.rickmortywiki.ui.data.BaseUiItem
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class BaseListViewModel<DataBM, DataUI : BaseUiItem, R : Repository<DataBM>>(
    private val repository: R,
    private val mapToUiModel: (DataBM) -> DataUI
) : KoinComponent {

    private var cachedData: MutableList<DataUI> = mutableListOf()
    private val compositeDisposable = CompositeDisposable()

    private val progressBarVisibility: PublishSubject<Boolean> = PublishSubject.create()
    private val snackBarMessage: PublishSubject<String> = PublishSubject.create()
    private val dataList: PublishSubject<List<DataUI>> = PublishSubject.create()
    fun getProgressBarVisibility(): Observable<Boolean> = progressBarVisibility
    fun showSnackbarMessage(): Observable<String> = snackBarMessage
    fun getDataList(): Observable<List<DataUI>> = dataList

    fun clear() {
        compositeDisposable.clear()
    }

    fun onSubscribe() {
        fetchData(true)
    }

    fun loadMoreData() {
        fetchData(false)
    }

    fun refresh() {
        cachedData.clear()
        repository.refresh()
        fetchData(false)
    }

    fun filterData(query: String): List<DataUI> {
        return cachedData
            .filter { it.name.contains(query, true) }
            .toList()
    }

    private fun fetchData(onSubscribe: Boolean) {
        if (!isNetworkAvailable()) {
            Log.d(TAG, "no network")
            progressBarVisibility.onNext(false)
            snackBarMessage.onNext("No network connection")
            return
        }

        progressBarVisibility.onNext(true)
        if (onSubscribe && cachedData.isNotEmpty()) {
            dataList.onNext(cachedData)
            progressBarVisibility.onNext(false)
        } else {
            // no cached data. call for repository
            Log.d(TAG, "launching request")
            val disposable = repository.getListData()
                .subscribeOn(Schedulers.io())
                .map { it.map { item -> mapToUiModel(item) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        //onNext
                        cachedData.addAll(it)
                        dataList.onNext(cachedData)
                    },
                    {
                        //onError
                        Log.d(TAG, "error $it")
                        progressBarVisibility.onNext(false)
                    },
                    {
                        //onComplete
                        Log.d(TAG, "complete")
                        progressBarVisibility.onNext(false)
                    })
            compositeDisposable.add(disposable)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val context: Context = get()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {
        private const val TAG = "RMBaseListVM"
    }
}