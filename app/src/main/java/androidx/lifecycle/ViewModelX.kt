package androidx.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import me.alfredobejarano.golfassistant.BuildConfig
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

private const val IO_JOB_KEY = "${BuildConfig.APPLICATION_ID}.IO_JOB_KEY"

/**
 *
 * [CoroutineScope] tied to this [ViewModel].
 * This scope will be canceled when ViewModel will be cleared, i.e [ViewModel.onCleared] is called
 *
 * This scope is bound to [Dispatchers.IO]
 *
 * Created by alfredo corona on 2019-06-03.
 * Copyright © 2019 GROW. All rights reserved.
 */
val ViewModel.ioViewModelScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(IO_JOB_KEY)
        return scope?.let { scope ->
            scope
        } ?: run {
            setTagIfAbsent(IO_JOB_KEY, CloseableIOCoroutineScope(SupervisorJob() + Dispatchers.IO))
        }
    }

internal class CloseableIOCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override fun close() = coroutineContext.cancel()
    override val coroutineContext: CoroutineContext = context
}