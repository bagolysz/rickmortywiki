package org.bagolysz.rickmortywiki.ui.feature.character


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_character_details.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.ui.util.Navigator
import org.bagolysz.rickmortywiki.util.GlideApp
import org.bagolysz.rickmortywiki.viewmodel.CharacterDetailsViewModel
import org.koin.android.ext.android.inject

/**
 * Screen to display details about a single character.
 *
 */
class CharacterDetailsFragment : Fragment() {

    private val viewModel: CharacterDetailsViewModel by inject()
    private val subscriptions = CompositeDisposable()
    private lateinit var adapter: CharacterDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigator.hideBottomNavigation()
        initViews()

        arguments?.let {
            viewModel.onSubscribe(it.getInt(ARG_CHAR_ID))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        viewModel.clear()
    }

    private fun initViews() {
        character_details_recycler_view.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CharacterDetailsAdapter(context!!)
        character_details_recycler_view.adapter = adapter

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_margin_standard))
        character_details_recycler_view.addItemDecoration(itemDecoration)

        subscriptions.add(
            viewModel.getProgressBarVisibility().subscribe {
                character_details_progress_bar.visibility = if (it) View.VISIBLE else View.GONE
                character_details_name_container.visibility = if (it) View.GONE else View.VISIBLE
                character_details_recycler_view.visibility = if (it) View.GONE else View.VISIBLE
            })
        subscriptions.add(
            viewModel.getCharacterData().subscribe {
                GlideApp.with(this)
                    .load(it.image)
                    .into(character_details_image_view)
                character_details_name.text = it.name
            })
        subscriptions.add(
            viewModel.getCharacterDetailsData().subscribe {
                adapter.setDataList(it)
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "RMCharacterDetailsFrag"
        private const val ARG_CHAR_ID = "ArgCharId"

        fun newInstance(characterId: Int): CharacterDetailsFragment {
            val fragment =
                CharacterDetailsFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_CHAR_ID, characterId)
            fragment.arguments = bundle
            return fragment
        }
    }
}