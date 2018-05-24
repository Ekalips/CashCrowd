package com.ekalips.cahscrowd.main.mvvm.view.child

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekalips.cahscrowd.R

class AccountingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_accounting, container, false)
    }

    companion object {
        fun newInstance() = AccountingFragment()
    }
}