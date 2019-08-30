package me.alfredobejarano.golfassistant.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.alfredobejarano.golfassistant.viewmodels.ScorecardListViewModel

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ScorecardListViewModel::class)
    abstract fun bindScorecardLitViewModel(viewModel: ScorecardListViewModel): ViewModel
}