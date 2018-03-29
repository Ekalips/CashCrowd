package com.ekalips.cahscrowd.main.mvvm.view.child

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.transition.AutoTransition
import android.support.transition.Transition
import android.support.transition.TransitionManager
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekalips.base.stuff.getBottomNavBarHeight
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.DialogAddEventBinding
import com.ekalips.cahscrowd.stuff.TransitionListenerAdapter

class CreateEventDialogFragment : DialogFragment() {

    lateinit var binding: DialogAddEventBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(context, R.style.UnlimitedDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_event, container, false)
        binding.backgroundView.setOnClickListener { dismiss() }
        collapseView()
        return binding.root
    }

    private fun collapseView(animate: Boolean = false, animationCallback: (() -> Unit)? = null) {
        if (animate) {
            val animation = AutoTransition()
            animationCallback?.let {
                animation.addListener(object : TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition) {
                        it()
                    }
                })
            }
            TransitionManager.beginDelayedTransition(binding.parentLayout, animation)
        }
        binding.backgroundView.alpha = 0F
        binding.titleTv.alpha = 0F

        val dp16 = resources.getDimension(R.dimen.d_16).toInt()
        val fabSize = resources.getDimension(R.dimen.fab_size).toInt()
        val createContainerLP = binding.createBtnContainer.layoutParams as ConstraintLayout.LayoutParams
        createContainerLP.width = fabSize
        createContainerLP.bottomToTop = -1
        createContainerLP.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        createContainerLP.horizontalBias = 1F
        createContainerLP.marginEnd = dp16
        createContainerLP.bottomMargin = dp16 + context.getBottomNavBarHeight() + dp16 / 2  // 8dp is the elevation of card holding bottom nav bar
        binding.createBtnContainer.requestLayout()
        binding.createBtnContainer.radius = fabSize / 2F

        binding.inviteBtnContainer.alpha = 0F
    }

    private fun expandView() {

    }

    override fun dismiss() {
        collapseView(true, { super.dismiss() })
    }


    companion object {
        fun newInstance() = CreateEventDialogFragment()
    }
}