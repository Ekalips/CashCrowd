package com.ekalips.cahscrowd.stuff.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.support.transition.Transition
import android.support.transition.TransitionListenerAdapter
import android.support.transition.TransitionValues
import android.support.v7.widget.CardView
import android.view.ViewGroup

class CardViewChangeRadiusTransition : Transition() {

    companion object {
        private const val PROP_NAME_RADIUS = "app:cardView:radius"
    }


    private fun captureValues(transitionValues: TransitionValues) {
        if (transitionValues.view is CardView) {
            transitionValues.values[PROP_NAME_RADIUS] = (transitionValues.view as CardView).radius
        }
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        val startRadius = startValues?.values?.get(PROP_NAME_RADIUS) as Float? ?: 0f
        val endRadius = endValues?.values?.get(PROP_NAME_RADIUS) as Float? ?: 0f
        return if (endValues?.view != null && endValues.view is CardView)
            createAnimation(endValues.view as CardView, startRadius, endRadius) else null
    }

    private fun createAnimation(view: CardView, startRadius: Float, endRadius: Float): Animator? {
        if (startRadius == endRadius) {
            return null
        }
        view.radius = startRadius
        val anim = ValueAnimator.ofFloat(startRadius, endRadius)
        val listener = ValueAnimator.AnimatorUpdateListener {
            view.radius = it.animatedValue as Float
        }
        anim.addUpdateListener(listener)
        addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                view.radius = endRadius
            }
        })
        return anim
    }
}