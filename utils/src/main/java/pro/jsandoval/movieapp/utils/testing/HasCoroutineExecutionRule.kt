package pro.jsandoval.movieapp.utils.testing

import org.junit.Rule

interface HasCoroutineExecutionRule {

    @Rule
    fun coroutineRule() = MainCoroutineRule()
}