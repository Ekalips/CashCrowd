package com.ekalips.cahscrowd.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ekalips.base.di.ViewModelKey
import com.ekalips.base.vm.BaseViewModelFactory
import com.ekalips.cahscrowd.auth.mvvm.vm.AuthScreenViewModel
import com.ekalips.cahscrowd.create_event.mvvm.vm.CreateEventScreenViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.child.EventsFragmentViewModel
import com.ekalips.cahscrowd.welcome.mvvm.vm.SplashScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**
 * Created by Ekalips on 2/7/18.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = SplashScreenViewModel::class)
    internal abstract fun bindSplasVM(splashScreenViewModel: SplashScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AuthScreenViewModel::class)
    internal abstract fun bindAuthVM(authScreenViewModel: AuthScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = MainScreenViewModel::class)
    internal abstract fun bindMainVM(mainScreenViewModel: MainScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = EventsFragmentViewModel::class)
    internal abstract fun bindEventsVM(maFragmentViewModel: EventsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = CreateEventScreenViewModel::class)
    internal abstract fun bindCreateEventVM(createEventScreenViewModel: CreateEventScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = EventScreenViewModel::class)
    internal abstract fun bindEventScreenVM(eventScreenViewModel: EventScreenViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}