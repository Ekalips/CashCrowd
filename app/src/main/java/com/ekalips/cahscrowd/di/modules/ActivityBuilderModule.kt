package com.ekalips.cahscrowd.di.modules


import com.ekalips.cahscrowd.auth.mvvm.view.AuthActivity
import com.ekalips.cahscrowd.di.scopes.ActivityScope
import com.ekalips.cahscrowd.main.di.MainFragmentsProvider
import com.ekalips.cahscrowd.main.mvvm.view.MainActivity
import com.ekalips.cahscrowd.welcome.mvvm.view.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Ekalips on 10/2/17.
 */

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun splashScreenActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun authActivity(): AuthActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainFragmentsProvider::class])
    internal abstract fun mainActivity(): MainActivity
}
