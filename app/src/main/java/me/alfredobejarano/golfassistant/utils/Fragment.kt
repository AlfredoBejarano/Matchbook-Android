package me.alfredobejarano.golfassistant.utils

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Fragment
 *
 * @author (c) AlfredoBejarano
 */

fun <T : ViewBinding> Fragment.viewBinding(init: (inflater: LayoutInflater) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        init {
            this@viewBinding.viewLifecycleOwnerLiveData.observe(
                this@viewBinding, Observer { owner: LifecycleOwner ->
                    owner.lifecycle.addObserver(this)
                })
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T = this.binding
            ?: if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED)
                error("Called before onCreateView or after onDestroyView.")
            else
                init(LayoutInflater.from(requireContext())).also {
                    this.binding = it
                }
    }