package org.bagolysz.rickmortywiki.ui.feature.character


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_base_list.*
import org.bagolysz.rickmortywiki.model.data.Character
import org.bagolysz.rickmortywiki.repository.Repository
import org.bagolysz.rickmortywiki.ui.base.BaseAdapter
import org.bagolysz.rickmortywiki.ui.base.BaseListFragment
import org.bagolysz.rickmortywiki.ui.base.Chamber
import org.bagolysz.rickmortywiki.ui.data.UiCharacterItem
import org.bagolysz.rickmortywiki.ui.util.Navigator
import org.bagolysz.rickmortywiki.ui.util.toPx
import org.bagolysz.rickmortywiki.viewmodel.BaseListViewModel
import org.koin.android.ext.android.get

/**
 * Screen to display a list of characters.
 *
 */
class CharacterListFragment : BaseListFragment<Character, UiCharacterItem>() {

    private var isSearchQueryVisible = false
    override lateinit var adapter: BaseAdapter<UiCharacterItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = CharacterListAdapter(context!!)
        retrieveViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Characters")
        initViews()
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "create menu")
        inflater.inflate(R.menu.menu_toolbar, menu)
        val searchViewItem = menu.findItem(R.id.toolbar_search)
        val searchView = searchViewItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "query submit: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "query change: $newText")
                viewModel?.filterData(newText ?: "")?.let {
                    adapter.setDataList(it)
                }
                return true
            }
        })
        searchView.setOnSearchClickListener {
            searchView.maxWidth = base_list_toolbar.width
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        searchView.setOnCloseListener {
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
            false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun onItemClick(itemId: Int) {
        Navigator.navigateToCharacterDetails(itemId)
    }

    private fun initViews() {
        setTitleVisibility(true)
        isSearchQueryVisible = false
        base_list_search_icon.visibility = View.VISIBLE
        base_list_search_icon.setOnClickListener {
            if (!isSearchQueryVisible) {
                isSearchQueryVisible = true
                base_list_search_icon.isEnabled = false
                setTitleVisibility(false)
                expandQueryEditText()
            }
        }
        base_list_close_search_icon.setOnClickListener {
            if (isSearchQueryVisible) {
                isSearchQueryVisible = false
                base_list_search_icon.isEnabled = true
                collapseQueryEditText()
            }
        }
        base_list_search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "query change: $s")
                viewModel?.filterData(s.toString())?.let {
                    adapter.setDataList(it)
                }
            }
        })
    }

    private fun expandQueryEditText() {
        val distance = base_list_appbar.width - 56.toPx()
        val searchSlide = ValueAnimator.ofFloat(0f, -distance.toFloat()).apply {
            duration = ANIMATION_DURATION
            interpolator = AnimationUtils.DECELERATE_INTERPOLATOR
            addUpdateListener {
                base_list_search_icon.translationX = it.animatedValue as Float
            }
        }
        val closeFade = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = ANIMATION_DURATION
            interpolator = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
            addUpdateListener {
                base_list_close_search_icon.alpha = it.animatedValue as Float
            }
        }

        base_list_close_search_icon.visibility = View.VISIBLE
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(searchSlide, closeFade)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                base_list_search_edit_text.visibility = View.VISIBLE
            }
        })
        animatorSet.start()
    }

    private fun collapseQueryEditText() {
        val distance = base_list_appbar.width - 56.toPx()
        val searchSlide = ValueAnimator.ofFloat(-distance.toFloat(), 0f).apply {
            duration = ANIMATION_DURATION
            interpolator = AnimationUtils.DECELERATE_INTERPOLATOR
            addUpdateListener {
                base_list_search_icon.translationX = it.animatedValue as Float
            }
        }
        val closeFade = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = ANIMATION_DURATION
            interpolator = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
            addUpdateListener {
                base_list_close_search_icon.alpha = it.animatedValue as Float
            }
        }

        base_list_close_search_icon.visibility = View.GONE
        base_list_search_edit_text.visibility = View.GONE
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(searchSlide, closeFade)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                setTitleVisibility(true)
            }
        })
        animatorSet.start()
    }

    private fun retrieveViewModel() {
        if (viewModel == null) {
            val chamber = ViewModelProviders.of(this).get(Chamber::class.java)
            if (chamber.stored == null) {
                chamber.stored =
                        BaseListViewModel(get<Repository<Character>>("repoCharacter")) { it.toUiModel() }
            }
            viewModel =
                    chamber.stored as BaseListViewModel<Character, UiCharacterItem, Repository<Character>>
        }
    }

    companion object {
        private const val TAG = "RMCharacterListFrag"

        private const val ANIMATION_DURATION = 170L
    }
}

fun Character.toUiModel(): UiCharacterItem =
    UiCharacterItem(
        id = 2,
        name = this.name,
        status = this.status,
        imageUrl = this.image
    )