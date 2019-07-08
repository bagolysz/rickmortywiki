package org.bagolysz.rickmortywiki.ui.feature.episode


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import org.bagolysz.rickmortywiki.model.data.Episode
import org.bagolysz.rickmortywiki.repository.Repository
import org.bagolysz.rickmortywiki.ui.base.BaseAdapter
import org.bagolysz.rickmortywiki.ui.base.BaseListFragment
import org.bagolysz.rickmortywiki.ui.base.Chamber
import org.bagolysz.rickmortywiki.ui.data.UiEpisodeItem
import org.bagolysz.rickmortywiki.viewmodel.BaseListViewModel
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 *
 */
class EpisodeListFragment : BaseListFragment<Episode, UiEpisodeItem>() {

    override lateinit var adapter: BaseAdapter<UiEpisodeItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = EpisodeListAdapter(context!!)
        retrieveViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Episodes")
    }

    override fun onItemClick(itemId: Int) {
        Toast.makeText(context, "Click on item $itemId", Toast.LENGTH_SHORT).show()
    }

    private fun retrieveViewModel() {
        if (viewModel == null) {
            val chamber = ViewModelProviders.of(this).get(Chamber::class.java)
            if (chamber.stored == null) {
                chamber.stored =
                        BaseListViewModel(get<Repository<Episode>>("repoEpisode")) { it.toUiModel() }
            }
            viewModel =
                    chamber.stored as BaseListViewModel<Episode, UiEpisodeItem, Repository<Episode>>
        }
    }

    companion object {
        private const val TAG = "RMEpisodeListFrag"
    }
}

fun Episode.toUiModel(): UiEpisodeItem =
    UiEpisodeItem(
        this.id,
        this.name,
        this.airDate,
        this.episode
    )