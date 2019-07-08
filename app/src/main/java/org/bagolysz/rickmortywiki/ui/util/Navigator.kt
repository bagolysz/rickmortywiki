package org.bagolysz.rickmortywiki.ui.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.feature.bookmark.BookmarksFragment
import org.bagolysz.rickmortywiki.ui.feature.character.CharacterDetailsFragment
import org.bagolysz.rickmortywiki.ui.feature.character.CharacterListFragment
import org.bagolysz.rickmortywiki.ui.feature.episode.EpisodeListFragment
import org.bagolysz.rickmortywiki.ui.feature.location.LocationListFragment
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

object Navigator : KoinComponent {

    var updateBottomNavigationHeight = true
        private set(value) {
            field = value
        }

    var fragmentManager: FragmentManager? = null
    var bottomNavigationController: BottomNavigationController? = null

    // bottom navigation view manipulation
    fun showBottomNavigation() {
        updateBottomNavigationHeight = true
        bottomNavigationController?.showBottomNavigationView()
    }

    fun hideBottomNavigation() {
        updateBottomNavigationHeight = false
        bottomNavigationController?.hideBottomNavigationView()
    }

    // navigation
    fun navigateToCharacterList() {
        val fragment = getBottomNavigationFragment(BottomNavOptions.CHARACTERS)
        navigate(fragment, false)
    }

    fun navigateToCharacterDetails(characterId: Int) {
        val fragment: CharacterDetailsFragment = get { parametersOf(characterId) }
        navigate(fragment, true)
    }

    fun navigateToLocationList() {
        val fragment = getBottomNavigationFragment(BottomNavOptions.LOCATIONS)
        navigate(fragment, false)
    }

    fun navigateToEpisodeList() {
        val fragment = getBottomNavigationFragment(BottomNavOptions.EPISODES)
        navigate(fragment, false)
    }

    fun navigateToBookmarksList() {
        val fragment = getBottomNavigationFragment(BottomNavOptions.BOOKMARKS)
        navigate(fragment, false)
    }

    private fun navigate(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.main_fragment_container, fragment)
        if (addToBackStack) {
            transaction?.addToBackStack("hello")
        }
        transaction?.commit()
    }

    private val navFragmentStore: MutableMap<BottomNavOptions, Fragment?> = hashMapOf(
        BottomNavOptions.CHARACTERS to null,
        BottomNavOptions.LOCATIONS to null,
        BottomNavOptions.EPISODES to null,
        BottomNavOptions.BOOKMARKS to null
    )

    private fun getBottomNavigationFragment(key: BottomNavOptions): Fragment {
        if (navFragmentStore[key] == null) {
            when (key) {
                BottomNavOptions.CHARACTERS -> navFragmentStore[key] = CharacterListFragment()
                BottomNavOptions.LOCATIONS -> navFragmentStore[key] = LocationListFragment()
                BottomNavOptions.EPISODES -> navFragmentStore[key] = EpisodeListFragment()
                BottomNavOptions.BOOKMARKS -> navFragmentStore[key] = BookmarksFragment()
            }
        }
        return navFragmentStore[key] ?: CharacterListFragment()
    }

    // for fragment persistence when using bottom navigation
    private enum class BottomNavOptions {
        CHARACTERS, LOCATIONS, EPISODES, BOOKMARKS
    }
}