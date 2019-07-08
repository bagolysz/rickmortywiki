package org.bagolysz.rickmortywiki.ui.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.animation.AnimationUtils

class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    private var currentState = STATE_SCROLLED_UP
    private var currentAnimator: ViewPropertyAnimator? = null

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
        if (this.currentState != STATE_SCROLLED_DOWN && dy > 0) {
            slideDown(child)
        } else if (this.currentState != STATE_SCROLLED_UP && dy < 0) {
            slideUp(child)
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is FrameLayout) {
            updateView(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    private fun updateView(child: View, dependent: FrameLayout) {
        if (dependent.layoutParams is CoordinatorLayout.LayoutParams) {
            val params = dependent.layoutParams as CoordinatorLayout.LayoutParams
            if (Navigator.updateBottomNavigationHeight) {
                params.bottomMargin = child.height - child.translationY.toInt()
            } else {
                params.bottomMargin = 0 //hide completely the bottom navigation view
            }
            dependent.layoutParams = params
        }
    }

    private fun min(a: Float, b: Float): Float {
        return if (a > b) b else a
    }

    private fun max(a: Float, b: Float): Float {
        return if (a > b) a else b
    }

    private fun slideUp(child: V) {
        currentAnimator?.let {
            it.cancel()
            child.clearAnimation()
        }
        currentState = STATE_SCROLLED_UP
        animateChildTo(child, 0, ANIMATION_DURATION, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
    }

    private fun slideDown(child: V) {
        currentAnimator?.let {
            it.cancel()
            child.clearAnimation()
        }
        currentState = STATE_SCROLLED_DOWN
        animateChildTo(
            child,
            child.height,
            ANIMATION_DURATION,
            AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
        )
    }


    private fun animateChildTo(
        child: V,
        targetY: Int,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        currentAnimator = child.animate().translationY(targetY.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    currentAnimator = null
                }
            })
    }

    companion object {
        private const val ANIMATION_DURATION = 225L
        private const val STATE_SCROLLED_DOWN = 1
        private const val STATE_SCROLLED_UP = 2
    }
}