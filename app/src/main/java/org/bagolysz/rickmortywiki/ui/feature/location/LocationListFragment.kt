package org.bagolysz.rickmortywiki.ui.feature.location


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import org.bagolysz.rickmortywiki.model.data.Location
import org.bagolysz.rickmortywiki.repository.Repository
import org.bagolysz.rickmortywiki.ui.base.BaseAdapter
import org.bagolysz.rickmortywiki.ui.base.BaseListFragment
import org.bagolysz.rickmortywiki.ui.base.Chamber
import org.bagolysz.rickmortywiki.ui.data.UiLocationItem
import org.bagolysz.rickmortywiki.viewmodel.BaseListViewModel
import org.koin.android.ext.android.get

/**
 * Screen to present a list of locations.
 *
 */
class LocationListFragment : BaseListFragment<Location, UiLocationItem>() {

    override lateinit var adapter: BaseAdapter<UiLocationItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = LocationListAdapter(context!!)
        retrieveViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Locations")
    }

    override fun onItemClick(itemId: Int) {
        Toast.makeText(context, "Click on item $itemId", Toast.LENGTH_SHORT).show()
    }

    private fun retrieveViewModel() {
        if (viewModel == null) {
            val chamber = ViewModelProviders.of(this).get(Chamber::class.java)
            if (chamber.stored == null) {
                chamber.stored =
                        BaseListViewModel(get<Repository<Location>>("repoLocation")) { it.toUiModel() }
            }
            viewModel =
                    chamber.stored as BaseListViewModel<Location, UiLocationItem, Repository<Location>>
        }
    }

    companion object {
        private const val TAG = "RMLocationListFrag"
    }
}

fun Location.toUiModel(): UiLocationItem =
    UiLocationItem(
        this.id,
        this.name,
        this.dimension,
        this.residents.size,
        this.type
    )