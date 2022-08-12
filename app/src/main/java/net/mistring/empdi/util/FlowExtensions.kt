package net.mistring.empdi.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Collect will collect every value , and CollectLatest will stop current work to collect latest value,
// The crucial difference from collect is that when the original flow emits a new value then the action block for the previous value is cancelled.

/**
 * Extension function to observe Flow via "collectLatest()"
 */
fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, action: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(action)
        }
    }
}


/**
 * Extension function to observe Flow via "collect()"
 */
fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collector: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collector)
        }
    }
}