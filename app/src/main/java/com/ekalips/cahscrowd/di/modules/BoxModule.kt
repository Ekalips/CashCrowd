package com.ekalips.cahscrowd.di.modules

import android.content.Context
import com.ekalips.cahscrowd.data.user.local.model.LocalBaseUser
import com.ekalips.cahscrowd.data.user.local.model.MyObjectBox
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
class BoxModule {

    @Provides
    @Singleton
    internal fun provideBoxStore(context: Context): BoxStore {
        return MyObjectBox.builder().androidContext(context).build()
    }

    @Provides
    @Singleton
    internal fun provideBaseUserBox(boxStore: BoxStore) = boxStore.boxFor(LocalBaseUser::class.java)

}