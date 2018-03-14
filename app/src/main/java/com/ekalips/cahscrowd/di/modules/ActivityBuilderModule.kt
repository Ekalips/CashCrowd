package com.ekalips.cahscrowd.di.modules


import com.ekalips.cahscrowd.welcome.mvvm.view.SplashActivity
import com.ekalips.cahscrowd.di.scopes.ActivityScope
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
}
