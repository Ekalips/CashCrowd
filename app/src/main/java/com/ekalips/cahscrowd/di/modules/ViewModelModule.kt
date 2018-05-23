package com.ekalips.cahscrowd.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ekalips.base.di.ViewModelKey
import com.ekalips.base.vm.BaseViewModelFactory
import com.ekalips.cahscrowd.auth.mvvm.vm.AuthScreenViewModel
import com.ekalips.cahscrowd.create_event.mvvm.vm.CreateEventScreenViewModel
import com.ekalips.cahscrowd.deep.DeepLinkViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.EventScreenViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.child.EventAccountingViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.child.EventActionsViewModel
import com.ekalips.cahscrowd.event.mvvm.vm.child.EventParticipantsViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.MainScreenViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.child.EventsFragmentViewModel
import com.ekalips.cahscrowd.main.mvvm.vm.child.ProfileFragmentViewModel
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
    @IntoMap
    @ViewModelKey(value = EventActionsViewModel::class)
    internal abstract fun bindEventActionsVM(eventActionsViewModel: EventActionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = EventParticipantsViewModel::class)
    internal abstract fun bindEventParticipantsVM(eventParticipantsViewModel: EventParticipantsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = DeepLinkViewModel::class)
    internal abstract fun bindDeepLinkViewModel(deepLinkViewModel: DeepLinkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = ProfileFragmentViewModel::class)
    internal abstract fun bindProfileVM(profileFragmentViewModel: ProfileFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = EventAccountingViewModel::class)
    internal abstract fun bindEventAccountingViewModel(vm: EventAccountingViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}