package me.alfredobejarano.golfassistant.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.alfredobejarano.golfassistant.MatchFragment
import me.alfredobejarano.golfassistant.ScorecardListFragment

@Module
abstract class UIControllerModule {
    @ContributesAndroidInjector
    abstract fun contributeScorecardListFragment(): ScorecardListFragment

    @ContributesAndroidInjector
    abstract fun contributeMatchFragment(): MatchFragment
}