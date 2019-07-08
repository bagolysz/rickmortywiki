package org.bagolysz.rickmortywiki.ui.feature.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.util.Navigator

/**
 * A simple [Fragment] subclass.
 *
 */
class BookmarksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigator.showBottomNavigation()
        (activity as AppCompatActivity).setSupportActionBar(bookmarks_toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Bookmarks"
    }
}