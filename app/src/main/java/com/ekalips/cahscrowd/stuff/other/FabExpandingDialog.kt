package com.ekalips.cahscrowd.stuff.other

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.*
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekalips.base.stuff.ifThen
import com.ekalips.cahscrowd.R
import com.ekalips.cahscrowd.databinding.DialogFabExpandBinding
import com.ekalips.cahscrowd.stuff.TransitionListenerAdapter
import com.ekalips.cahscrowd.stuff.transition.CardViewChangeRadiusTransition
import com.ekalips.cahscrowd.stuff.utils.disposeBy
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class FabExpandingDialog : DialogFragment() {

    private lateinit var binding: DialogFabExpandBinding

    private val disposer = CompositeDisposable()

    private var dismissed = false
    private var isCollapseInProgress = false
    private var isExpandInProgress = false

    private var fabBottomMargin = 0f
    private var fabEndMargin = 0f
    private var firstActionName: String = ""

    private var fragmentCallback: Reference<Callback>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(context, R.style.UnlimitedDialog).also {
            it.setOnShowListener { fragmentCallback?.get()?.onDialogOpen() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fab_expand, container, false)
        setUpViews(binding, arguments ?: savedInstanceState)
        RxView.clicks(binding.backgroundView).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { dismiss() }.disposeBy(disposer)
        RxView.clicks(binding.firstBtn).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { onFirstOptionSelected() }.disposeBy(disposer)
        RxView.clicks(binding.secondBtn).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { onSecondOptionSelected() }.disposeBy(disposer)
        collapseView()
        return binding.root.also { it.post { expandView() } }
    }

    private fun setUpViews(binding: DialogFabExpandBinding, args: Bundle?) {
        args?.let {
            binding.titleTv.text = args.getString(ARG_TITLE)
            binding.firstBtn.text = args.getString(ARG_FIRST_ACTION)
            binding.secondBtn.text = args.getString(ARG_SECOND_ACTION)

            val firstActionImg = args.getInt(ARG_FIRST_ACTION_IMG, 0)
            (firstActionImg != 0).ifThen { binding.firstBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(firstActionImg, 0, 0, 0) }

            val secondActionImg = args.getInt(ARG_SECOND_ACTION_IMG, 0)
            (secondActionImg != 0).ifThen { binding.secondBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(secondActionImg, 0, 0, 0) }

            fabBottomMargin = args.getFloat(ARG_INITIAL_BOTTOM_MARGIN)
            fabEndMargin = args.getFloat(ARG_INITIAL_END_MARGIN)

            firstActionName = binding.secondBtn.text.toString()
        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        onClosed()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onClosed()
    }

    private fun onClosed() {
        fragmentCallback?.get()?.onDialogClose()
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
        binding.secondBtnContainer.visibility = View.INVISIBLE

        val dp16 = resources.getDimension(R.dimen.d_16).toInt()
        val fabSize = resources.getDimension(R.dimen.fab_size).toInt()
        val createContainerLP = binding.firstBtnContainer.layoutParams as ConstraintLayout.LayoutParams
        createContainerLP.width = fabSize

        val constraintSet = ConstraintSet().also { it.clone(binding.parentLayout) }
        constraintSet.connect(binding.firstBtnContainer.id, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.setMargin(binding.firstBtnContainer.id, ConstraintSet.END, fabEndMargin.toInt())
        constraintSet.setMargin(binding.firstBtnContainer.id, ConstraintSet.BOTTOM, fabBottomMargin.toInt())
        constraintSet.clear(binding.firstBtnContainer.id, ConstraintSet.LEFT)
        constraintSet.connect(binding.firstBtnContainer.id, ConstraintSet.RIGHT, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.RIGHT)
        constraintSet.applyTo(binding.parentLayout)

        binding.firstBtnContainer.radius = fabSize / 2F
        binding.firstBtn.text = ""
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
        constraintSet.connect(binding.firstBtnContainer.id, ConstraintSet.BOTTOM, binding.buttonsGuideline.id, ConstraintSet.BOTTOM)
        constraintSet.setMargin(binding.firstBtnContainer.id, ConstraintSet.END, 0)
        constraintSet.setMargin(binding.firstBtnContainer.id, ConstraintSet.BOTTOM, resources.getDimension(R.dimen.d_16).toInt())
        constraintSet.connect(binding.firstBtnContainer.id, ConstraintSet.LEFT, binding.centerGuideline.id, ConstraintSet.LEFT)
        constraintSet.connect(binding.firstBtnContainer.id, ConstraintSet.RIGHT, binding.centerGuideline.id, ConstraintSet.RIGHT)
        constraintSet.applyTo(binding.parentLayout)
    }


    private fun expandPartTwo() {
        val createContainerLP = binding.firstBtnContainer.layoutParams as ConstraintLayout.LayoutParams
        createContainerLP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        binding.firstBtnContainer.requestLayout()
        binding.firstBtn.text = firstActionName
        binding.firstBtnContainer.radius = resources.getDimension(R.dimen.d_4)
    }

    private fun expandPartThree() {
        binding.backgroundView.visibility = View.VISIBLE
        binding.titleTv.visibility = View.VISIBLE
        binding.secondBtnContainer.visibility = View.VISIBLE
    }

    override fun dismiss() {
        if (!dismissed) {
            collapseView(true, {
                (!dismissed).ifThen {
                    dismissed = true
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

    private fun onFirstOptionSelected() {
        fragmentCallback?.get()?.onFirstOptionSelected()
        dismiss()
    }

    private fun onSecondOptionSelected() {
        fragmentCallback?.get()?.onSecondOptionSelected()
        dismiss()
    }

    fun setCallback(callback: Callback) {
        this.fragmentCallback = WeakReference(callback)
    }


    interface Callback {
        fun onDialogOpen()
        fun onDialogClose()
        fun onFirstOptionSelected()
        fun onSecondOptionSelected()
    }

    companion object {
        const val TAG = "FabExpandingDialog"

        private const val ARG_TITLE = "title"
        private const val ARG_FIRST_ACTION = "action_1"
        private const val ARG_SECOND_ACTION = "action_2"
        private const val ARG_FIRST_ACTION_IMG = "action_1_img"
        private const val ARG_SECOND_ACTION_IMG = "action_2_img"
        private const val ARG_INITIAL_END_MARGIN = "fab_end_margin"
        private const val ARG_INITIAL_BOTTOM_MARGIN = "fab_bottom_margin"

        fun newInstance(title: String, firstAction: String, secondAction: String, firstActionImg: Int, secondActionImg: Int,
                        initialBottomMargin: Float, initialEndMargin: Float): FabExpandingDialog {
            val fragment = FabExpandingDialog()
            val args = Bundle(3)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_FIRST_ACTION, firstAction)
            args.putString(ARG_SECOND_ACTION, secondAction)
            args.putInt(ARG_FIRST_ACTION_IMG, firstActionImg)
            args.putInt(ARG_SECOND_ACTION_IMG, secondActionImg)
            args.putFloat(ARG_INITIAL_BOTTOM_MARGIN, initialBottomMargin)
            args.putFloat(ARG_INITIAL_END_MARGIN, initialEndMargin)
            fragment.arguments = args
            return fragment
        }

        private const val DEFAULT_ANIM_SPEED = 250L

        class FabExpandingDialogBuilder private constructor(private val context: Context) {
            private var title = ""
            private var firstActionName = ""
            private var secondActionName = ""
            private var firstActionImg = 0
            private var secondActionImg = 0
            private var initialBottomMargin = 0f
            private var initialEndMargin = 0f

            fun setTitle(title: String): FabExpandingDialogBuilder {
                this.title = title
                return this
            }

            fun setTitle(@StringRes titleRes: Int) = setTitle(context.getString(titleRes))

            fun setFirstActionName(name: String): FabExpandingDialogBuilder {
                this.firstActionName = name
                return this
            }

            fun setFirstActionName(@StringRes nameRes: Int) = setFirstActionName(context.getString(nameRes))

            fun setSecondActionName(name: String): FabExpandingDialogBuilder {
                this.secondActionName = name
                return this
            }

            fun setSecondActionName(@StringRes nameRes: Int) = setSecondActionName(context.getString(nameRes))

            fun setFirstActionImg(@DrawableRes img: Int): FabExpandingDialogBuilder {
                firstActionImg = img
                return this
            }

            fun setSecondActionImg(@DrawableRes img: Int): FabExpandingDialogBuilder {
                secondActionImg = img
                return this
            }

            fun setInitialFabBottomMargin(margin: Float): FabExpandingDialogBuilder {
                initialBottomMargin = margin
                return this
            }

            fun setInitialFabEndMargin(margin: Float): FabExpandingDialogBuilder {
                initialEndMargin = margin
                return this
            }

            fun build(): FabExpandingDialog {
                return newInstance(title, firstActionName, secondActionName, firstActionImg, secondActionImg, initialBottomMargin, initialEndMargin)
            }

            companion object {
                fun create(context: Context) = FabExpandingDialogBuilder(context)
            }
        }
    }
}