package com.ekalips.cahscrowd.event.di

import android.support.v4.app.FragmentManager
import com.ekalips.cahscrowd.di.scopes.ActivityScope
import com.ekalips.cahscrowd.di.scopes.FragmentScope
import com.ekalips.cahscrowd.event.mvvm.view.EventActivity
import com.ekalips.cahscrowd.event.mvvm.view.child.EventActionsFragment
import com.ekalips.cahscrowd.event.mvvm.view.child.EventParticipantsFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [EventFragmentsProvider.Fragments::class])
class EventFragmentsProvider {

    @Provides
    @ActivityScope
    fun provideFragmentManager(activity: EventActivity): FragmentManager {
        return activity.supportFragmentManager
    }

    @Module
    interface Fragments {
        @FragmentScope
        @ContributesAndroidInjector
        fun actionsFragment(): EventActionsFragment

        @FragmentScope
        @ContributesAndroidInjector
        fun participantsFragment(): EventParticipantsFragment
    }

}