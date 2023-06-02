package pro.jsandoval.movieapp.utils.testing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

interface HasInstantExecutionRule {

    @Rule
    fun instantExecutionRule() = InstantTaskExecutorRule()
}