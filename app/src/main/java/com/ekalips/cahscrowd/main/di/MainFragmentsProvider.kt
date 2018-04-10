package com.ekalips.cahscrowd.main.di

import android.support.v4.app.FragmentManager
import com.ekalips.cahscrowd.di.scopes.ActivityScope
import com.ekalips.cahscrowd.di.scopes.FragmentScope
import com.ekalips.cahscrowd.main.mvvm.view.child.EventsFragment
import com.ekalips.cahscrowd.main.mvvm.view.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainFragmentsProvider.Fragments::class])
class MainFragmentsProvider {

    @Provides
    @ActivityScope
    fun provideFragmentManager(mainActivity: MainActivity): FragmentManager {
        return mainActivity.supportFragmentManager
    }

    @Module
    interface Fragments {
        @FragmentScope
        @ContributesAndroidInjector
        fun eventsFragment(): EventsFragment
    }

}