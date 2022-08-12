package net.mistring.empdi.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseUnitTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

}