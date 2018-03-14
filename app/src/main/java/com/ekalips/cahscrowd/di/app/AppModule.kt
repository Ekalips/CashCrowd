package com.ekalips.cahscrowd.di.app

import android.app.Application
import android.content.Context
import com.ekalips.cahscrowd.di.modules.BoxModule
import com.ekalips.cahscrowd.di.modules.NetworkModule
import com.ekalips.cahscrowd.di.modules.ViewModelModule
import dagger.Binds
import dagger.Module

/**
 * Created by Ekalips on 10/2/17.
 */

@Module(includes = [ViewModelModule::class, BoxModule::class, NetworkModule::class])
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context
}
