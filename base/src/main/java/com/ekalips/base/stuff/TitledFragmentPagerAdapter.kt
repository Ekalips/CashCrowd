package com.ekalips.base.stuff

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TitledFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val items = ArrayList<Pair<String, Fragment>>()
    private val lock = Any()

    override fun getItem(position: Int): Fragment = items[position].second
    override fun getPageTitle(position: Int): CharSequence? = items[position].first
    override fun getCount(): Int = items.size


    fun addItem(newItem: Pair<String, Fragment>) {
        synchronized(lock) {
            items.add(newItem.copy())
        }
        notifyDataSetChanged()
    }

    fun addItem(fragment: Fragment, title: String) {
        synchronized(lock) {
            items.add(title to fragment)
        }
        notifyDataSetChanged()
    }

    fun removeItem(fragment: Fragment) {
        synchronized(lock) {
            items.removeAll { it.second === fragment }
        }
        notifyDataSetChanged()
    }

    fun removeItem(title: String) {
        synchronized(lock) {
            items.removeAll { it.first == title }
        }
        notifyDataSetChanged()
    }
}