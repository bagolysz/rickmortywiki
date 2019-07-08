package org.bagolysz.rickmortywiki.util

import org.bagolysz.rickmortywiki.model.data.Character
import org.bagolysz.rickmortywiki.model.data.Episode
import org.bagolysz.rickmortywiki.model.data.Location
import org.bagolysz.rickmortywiki.repository.CharacterRepo
import org.bagolysz.rickmortywiki.repository.Repository
import org.bagolysz.rickmortywiki.repository.network.CharacterNetworkRepository
import org.bagolysz.rickmortywiki.repository.network.EpisodeNetworkRepository
import org.bagolysz.rickmortywiki.repository.network.LocationNetworkRepository
import org.bagolysz.rickmortywiki.repository.network.NetworkApi
import org.bagolysz.rickmortywiki.ui.feature.character.CharacterDetailsFragment
import org.bagolysz.rickmortywiki.viewmodel.CharacterDetailsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// modules
val appModule = module {
}

val presenterModule = module {
    factory {
        CharacterDetailsViewModel(CharacterNetworkRepository(get()))
    }
}

val fragmentModule = module {
    factory { (id: Int) -> CharacterDetailsFragment.newInstance(id) }
}

val repositoryModule = module {
    single<Repository<Character>>(name = "repoCharacter") {
        CharacterRepo(
            CharacterNetworkRepository(
                get()
            )
        )
    }
    single<Repository<Location>>(name = "repoLocation") { LocationNetworkRepository(get()) }
    single<Repository<Episode>>(name = "repoEpisode") { EpisodeNetworkRepository(get()) }
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(NetworkApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NetworkApi::class.java)
    }
}