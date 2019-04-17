package com.example.gitrepos

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.gitrepos.TestUtility.Companion.getTestGitRepoData
import com.example.gitrepos.data.*
import com.example.gitrepos.network.model.ErrorResponse
import com.example.gitrepos.network.model.GitRepo
import com.example.gitrepos.util.Constants
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit


class GitRepoViewModelTest {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var dataRepository: DataRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    lateinit var gitRepoViewModel: GitRepoViewModel

    @Before
    fun setUp() {
        gitRepoViewModel = GitRepoViewModel(dataRepository)
    }


    @Test
    fun testViewModelLoading() {

        val liveDataUnderTest = gitRepoViewModel.getGitRepoData()?.testObserver()
        gitRepoViewModel.getGitRepo()

        assertEquals(liveDataUnderTest?.observedValues?.size, 1)

        //Test loading state
        val gitRepoDataLoading = liveDataUnderTest?.observedValues?.get(0) as Data
        assertTrue(gitRepoDataLoading is Data.Loading)
        assertFalse(gitRepoDataLoading is Data.Success)
        assertFalse(gitRepoDataLoading is Data.Error)
    }

    @Test
    fun testViewModelSuccess() {

        val testGitRepoData = getTestGitRepoData()

        whenever(dataRepository.getGitRepo(any(), any())).thenAnswer {
            (it.getArgument(0) as (List<GitRepo>?) -> (Unit)).invoke(getTestGitRepoData())
        }

        val liveDataUnderTest = gitRepoViewModel.getGitRepoData()?.testObserver()

        gitRepoViewModel.getGitRepo()

        assertEquals(liveDataUnderTest?.observedValues?.size, 2)

        //Test success state
        val dataSuccess = liveDataUnderTest?.observedValues?.get(1) as Data
        assertTrue(dataSuccess is Data.Success)
        assertEquals((dataSuccess as Data.Success).gitData, testGitRepoData)
        assertFalse(dataSuccess is Data.Error)
        assertFalse(dataSuccess is Data.Loading)
    }

    @Test
    fun testViewModelError() {

        whenever(dataRepository.getGitRepo(any(), any())).thenAnswer {
            (it.getArgument(1) as (ErrorResponse) -> (Unit)).invoke((ErrorResponse(Throwable())))
        }

        val liveDataUnderTest = gitRepoViewModel.getGitRepoData()?.testObserver()

        gitRepoViewModel.getGitRepo()

        assertEquals(liveDataUnderTest?.observedValues?.size, 2)

        //Test success state
        val dataError = liveDataUnderTest?.observedValues?.get(1) as Data
        assertTrue(dataError is Data.Error)
        assertFalse(dataError is Data.Success)
        assertFalse(dataError is Data.Loading)
        assertNotNull((dataError as Data.Error).error.errorMessage)
        assertEquals((dataError as Data.Error).error.errorMessage, Constants.ERROR_MESSAGE)
    }


    @Test
    fun testViewModelInitialState() {

        val liveData = gitRepoViewModel.getGitRepoData()
        val data = liveData?.value
        assertFalse(data is Data.Error)
        assertFalse(data is Data.Success)
        assertFalse(data is Data.Loading)
    }

}

