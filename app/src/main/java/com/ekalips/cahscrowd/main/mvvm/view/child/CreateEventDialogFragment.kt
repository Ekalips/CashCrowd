package com.ekalips.cahscrowd.main.mvvm.view.child

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.*
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekalips.base.stuff.getBottomNavBarHeight
import com.ekalips.base.stuff.ifThen
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.DialogAddEventBinding
import com.ekalips.cahscrowd.stuff.TransitionListenerAdapter
import com.ekalips.cahscrowd.stuff.transition.CardViewChangeRadiusTransition

class CreateEventDialogFragment : DialogFragment() {

    lateinit var binding: DialogAddEventBinding

    var dissmissed = false
    var isCollapseInProgress = false
    var isExpandInProgress = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(context, R.style.UnlimitedDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_event, container, false)
        binding.backgroundView.setOnClickListener { dismiss() }
        collapseView()
        return binding.root.also { it.post { expandView() } }
    }


    private fun collapseView(animate: Boolean = false, animationCallback: (() -> Unit)? = null) {
        if (animate && !isCollapseInProgress) {
            val animation = getChangeTransition(200)
            animationCallback?.let {
                animation.addListener(object : TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition) {
                        isCollapseInProgress = false
                        it()
                    }
                })
            }
            isCollapseInProgress = true
            TransitionManager.beginDelayedTransition(binding.parentLayout, animation)
        }
        binding.backgroundView.visibility = View.INVISIBLE
        binding.titleTv.visibility = View.INVISIBLE
        binding.inviteBtnContainer.visibility = View.INVISIBLE

        val dp16 = resources.getDimension(R.dimen.d_16).toInt()
        val fabSize = resources.getDimension(R.dimen.fab_size).toInt()
        val createContainerLP = binding.createBtnContainer.layoutParams as ConstraintLayout.LayoutParams
        createContainerLP.width = fabSize

        val constraintSet = ConstraintSet().also { it.clone(binding.parentLayout) }
        constraintSet.connect(binding.createBtnContainer.id, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.setMargin(binding.createBtnContainer.id, ConstraintSet.END, dp16)
        constraintSet.setMargin(binding.createBtnContainer.id, ConstraintSet.BOTTOM, dp16 + context.getBottomNavBarHeight() + dp16 / 2) // 8dp is the elevation of card holding bottom nav bar
        constraintSet.clear(binding.createBtnContainer.id, ConstraintSet.LEFT)
        constraintSet.connect(binding.createBtnContainer.id, ConstraintSet.RIGHT, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.RIGHT)
        constraintSet.applyTo(binding.parentLayout)

        binding.createBtnContainer.radius = fabSize / 2F
        binding.createBtn.text = ""

    }

    private fun expandView() {
        if (!isExpandInProgress) {
            isExpandInProgress = true
            animateChange(change = { expandPartOne() }, endCallback = {
                animateChange(change = { expandPartTwo() })
                animateChange(transition = Fade(), delay = DEFAULT_ANIM_SPEED / 3, change = { expandPartThree() }, endCallback = { isExpandInProgress = false })
            })
        }
    }

    private fun expandPartOne() {
        val constraintSet = ConstraintSet().also { it.clone(binding.parentLayout) }
        constraintSet.connect(binding.createBtnContainer.id, ConstraintSet.BOTTOM, binding.buttonsGuideline.id, ConstraintSet.BOTTOM)
        constraintSet.setMargin(binding.createBtnContainer.id, ConstraintSet.END, 0)
        constraintSet.setMargin(binding.createBtnContainer.id, ConstraintSet.BOTTOM, resources.getDimension(R.dimen.d_16).toInt())
        constraintSet.connect(binding.createBtnContainer.id, ConstraintSet.LEFT, binding.centerGuideline.id, ConstraintSet.LEFT)
        constraintSet.connect(binding.createBtnContainer.id, ConstraintSet.RIGHT, binding.centerGuideline.id, ConstraintSet.RIGHT)
        constraintSet.applyTo(binding.parentLayout)
    }


    private fun expandPartTwo() {
        val createContainerLP = binding.createBtnContainer.layoutParams as ConstraintLayout.LayoutParams
        createContainerLP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        binding.createBtnContainer.requestLayout()
        binding.createBtn.setText(R.string.create_event_btn_text)
        binding.createBtnContainer.radius = resources.getDimension(R.dimen.d_4)
    }

    private fun expandPartThree() {
        binding.backgroundView.visibility = View.VISIBLE
        binding.titleTv.visibility = View.VISIBLE
        binding.inviteBtnContainer.visibility = View.VISIBLE
    }

    override fun dismiss() {
        if (!dissmissed) {
            collapseView(true, {
                (!dissmissed).ifThen {
                    dissmissed = true
                    super.dismiss()
                }
            })
        }
    }

    private fun getChangeTransition(speed: Long = DEFAULT_ANIM_SPEED): Transition =
            TransitionSet()
                    .addTransition(AutoTransition().also { it.setPathMotion(ArcMotion()) })
                    .addTransition(CardViewChangeRadiusTransition())
                    .setOrdering(TransitionSet.ORDERING_TOGETHER)
                    .setDuration(speed)


    private fun animateChange(transition: Transition = getChangeTransition(),
                              delay: Long = 0,
                              change: () -> Unit, endCallback: (() -> Unit)? = null) {
        transition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                endCallback?.invoke()
            }
        })
        transition.startDelay = delay
        TransitionManager.beginDelayedTransition(binding.parentLayout, transition)
        change()
    }

    companion object {
        fun newInstance() = CreateEventDialogFragment()
        private const val DEFAULT_ANIM_SPEED = 250L
    }
}