package org.bagolysz.rickmortywiki.viewmodel

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.bagolysz.rickmortywiki.model.data.Character
import org.bagolysz.rickmortywiki.repository.network.NetworkRepository
import org.bagolysz.rickmortywiki.ui.data.UiCharacterDetailItem

class CharacterDetailsViewModel(private val characterRepository: NetworkRepository<Character>) {

    private var cachedData: Character? = null

    private val compositeDisposable = CompositeDisposable()
    private val progressBarVisibility: PublishSubject<Boolean> = PublishSubject.create()
    private val characterData: PublishSubject<Character> = PublishSubject.create()
    private val characterDetailsData: PublishSubject<List<UiCharacterDetailItem>> =
        PublishSubject.create()

    fun getProgressBarVisibility(): Observable<Boolean> = progressBarVisibility

    fun getCharacterData(): Observable<Character> = characterData

    fun getCharacterDetailsData(): Observable<List<UiCharacterDetailItem>> = characterDetailsData

    fun onSubscribe(characterId: Int) {
        getCharacter(characterId)
    }

    fun clear() {
        compositeDisposable.clear()
    }

    private fun getCharacter(characterId: Int) {
        progressBarVisibility.onNext(true)
        if (cachedData != null) {
            characterData.onNext(cachedData!!)
            progressBarVisibility.onNext(false)
        } else {
            // no cached data. Launch request
            Log.d(TAG, "launching request")
            val disposable = characterRepository.getItemById(characterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        //onNext
                        cachedData = it
                        characterData.onNext(it)
                        characterDetailsData.onNext(it.toUiDetailList())
                        // log
                        Log.d(TAG, it.toString())
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

    companion object {
        private const val TAG = "RMCharacterDetailsVM"
    }

}

fun Character.toUiDetailList(): List<UiCharacterDetailItem> {
    val list: MutableList<UiCharacterDetailItem> = mutableListOf()
    list.add(
        UiCharacterDetailItem(
            elementDescription = "Status",
            status = this.status
        )
    )
    list.add(
        UiCharacterDetailItem(
            elementDescription = "Species",
            status = this.species
        )
    )
    list.add(
        UiCharacterDetailItem(
            elementDescription = "Gender",
            status = this.gender
        )
    )
    list.add(
        UiCharacterDetailItem(
            elementDescription = "Origin",
            status = this.origin.name
        )
    )
    list.add(
        UiCharacterDetailItem(
            elementDescription = "Last\nlocation",
            status = this.location.name
        )
    )
    return list
}