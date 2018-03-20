package com.ekalips.cahscrowd.di.modules

import android.content.Context
import com.ekalips.cahscrowd.data.db.CashDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {


    @Provides
    @Singleton
    fun provideDB(context: Context): CashDB =
            CashDB.create(context)

    @Provides
    @Singleton
    fun provideUsersDao(cashDB: CashDB) =
            cashDB.users()

    @Provides
    @Singleton
    fun provideActionsDao(cashDB: CashDB) =
            cashDB.actions()

    @Provides
    @Singleton
    fun proivdeEventsDao(cashDB: CashDB) =
            cashDB.events()

}