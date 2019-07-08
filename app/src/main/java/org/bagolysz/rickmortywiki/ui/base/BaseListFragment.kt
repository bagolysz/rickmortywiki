package org.bagolysz.rickmortywiki.ui.base

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_base_list.*
import org.bagolysz.rickmortywiki.R
import org.bagolysz.rickmortywiki.repository.Repository
import org.bagolysz.rickmortywiki.ui.data.BaseUiItem
import org.bagolysz.rickmortywiki.ui.util.Navigator
import org.bagolysz.rickmortywiki.viewmodel.BaseListViewModel

abstract class BaseListFragment<DataBM, DataUI : BaseUiItem> : Fragment(),
    BaseAdapter.ItemClickListener {

    private var currentState = STATE_SCROLLED_DOWN

    private val subscriptions = CompositeDisposable()

    var viewModel: BaseListViewModel<DataBM, DataUI, Repository<DataBM>>? = null

    abstract var adapter: BaseAdapter<DataUI>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigator.showBottomNavigation()
        currentState = STATE_SCROLLED_DOWN
        initViews()
        viewModel?.onSubscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        viewModel?.clear()
    }

    override fun onLoadMoreItemsClick() {
        viewModel?.loadMoreData()
    }

    protected fun setTitle(title: String) {
        base_list_toolbar_title.text = title
    }

    protected fun setTitleVisibility(visible: Boolean) {
        base_list_toolbar_title.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun initViews() {
        base_list_recycler_view.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        base_list_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    base_list_fab.hide()
                } else if (dy < 0) {
                    base_list_fab.show()
                } else {
                    base_list_fab.hide()
                }
            }
        })
        adapter.setItemClickListener(this)
        base_list_recycler_view.adapter = adapter
        base_list_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (currentState != STATE_SCROLLED_UP && dy > 0) {
                    slideUp()
                } else if (currentState != STATE_SCROLLED_DOWN && dy < 0) {
                    slideDown()
                }
            }
        })

        base_list_fab.setOnClickListener { smoothScrollToTop() }
        base_list_progress_bar.setOnRefreshListener { viewModel?.refresh() }

        viewModel?.getProgressBarVisibility()?.subscribe {
            base_list_progress_bar.isRefreshing = it
        }?.let { subscriptions.add(it) }

        viewModel?.showSnackbarMessage()?.subscribe {
            showSnackbar(it)
        }?.let { subscriptions.add(it) }

        viewModel?.getDataList()?.subscribe {
            adapter.setDataList(it)
        }?.let { subscriptions.add(it) }
    }

    private fun smoothScrollToTop() {
        val layoutManager = base_list_recycler_view.layoutManager as? LinearLayoutManager
        val firstVisibleItemPos = layoutManager?.findFirstVisibleItemPosition() ?: 0

        if (firstVisibleItemPos < 10) {
            base_list_recycler_view.smoothScrollToPosition(0)
        } else {
            base_list_recycler_view.scrollToPosition(10)
            base_list_recycler_view.smoothScrollToPosition(0)
        }
    }

    protected fun showSnackbar(message: String) {
        Log.d(TAG, "show snackbar: $message")
        Snackbar.make(base_list_container, message, Snackbar.LENGTH_LONG).show()
    }

    private fun slideUp() {
        currentState = STATE_SCROLLED_UP
        val height = base_list_appbar.height
        ValueAnimator.ofInt(0, -height).apply {
            duration = ANIMATION_DURATION
            interpolator = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
            addUpdateListener {
                base_list_appbar.translationY = (it.animatedValue as Int).toFloat()
                base_list_recycler_view.setPadding(0, height + (it.animatedValue as Int), 0, 0)
            }
            start()
        }
    }

    private fun slideDown() {
        currentState = STATE_SCROLLED_DOWN
        val height = base_list_appbar.height
        ValueAnimator.ofInt(-height, 0).apply {
            duration = ANIMATION_DURATION
            interpolator = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
            addUpdateListener {
                base_list_appbar.translationY = (it.animatedValue as Int).toFloat()
                base_list_recycler_view.setPadding(
                    0,
                    height + (it.animatedValue as Int),
                    0,
                    0
                )
            }
            start()
        }
    }

    companion object {
        private const val TAG = "RMBaseListFragment"

        private const val ANIMATION_DURATION = 225L
        private const val STATE_SCROLLED_DOWN = 1
        private const val STATE_SCROLLED_UP = 2
    }
}