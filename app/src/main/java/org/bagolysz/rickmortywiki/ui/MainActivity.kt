package org.bagolysz.rickmortywiki.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.util.BottomNavigationController
import org.bagolysz.rickmortywiki.ui.util.Navigator

class MainActivity : AppCompatActivity(), BottomNavigationController {

    private var currentPageId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigator.bottomNavigationController = this
        Navigator.fragmentManager = supportFragmentManager

        if (savedInstanceState == null) {
            navigateToPage()
        }

        main_bottom_navigation_view.setOnNavigationItemSelectedListener {
            currentPageId = it.itemId
            navigateToPage()
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun showBottomNavigationView() {
        main_bottom_navigation_view.visibility = View.VISIBLE
    }

    override fun hideBottomNavigationView() {
        main_bottom_navigation_view.visibility = View.GONE
    }

    private fun navigateToPage() {
        when (currentPageId) {
            R.id.menu_item_characters -> {
                Navigator.navigateToCharacterList()
            }
            R.id.menu_item_locations -> {
                Navigator.navigateToLocationList()
            }
            R.id.menu_item_episodes -> {
                Navigator.navigateToEpisodeList()
            }
            R.id.menu_item_bookmarks -> {
                Navigator.navigateToBookmarksList()
            }
            else -> {
                Navigator.navigateToCharacterList()
            }
        }
    }
}