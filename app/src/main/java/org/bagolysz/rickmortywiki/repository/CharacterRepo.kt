package org.bagolysz.rickmortywiki.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import io.reactivex.Observable
import org.bagolysz.rickmortywiki.model.data.Character
import org.bagolysz.rickmortywiki.repository.local.LocalDatabase
import org.koin.standalone.KoinComponent
import org.koin.standalone.get


class CharacterRepo(private val networkRepo: Repository<Character>) : Repository<Character>,
    KoinComponent {

    private val db = Room.databaseBuilder(get(), LocalDatabase::class.java, "rm-local-db").build()

    override fun getListData(): Observable<List<Character>> {
        return networkRepo.getListData()
        /*if (isNetworkAvailable()) {
            Log.d("SZABI", "network available")
            return networkRepo.getListData().map {
                db.characterDao().insert(*it.toTypedArray())
                it
            }
        }
        Log.d("SZABI", "network not available")
        return db.characterDao().getAll()*/
    }

    override fun getItemById(id: Int): Observable<Character> {
        return networkRepo.getItemById(id)
    }

    override fun refresh() {
        networkRepo.refresh()
    }

    private fun isNetworkAvailable(): Boolean {
        val context: Context = get()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}