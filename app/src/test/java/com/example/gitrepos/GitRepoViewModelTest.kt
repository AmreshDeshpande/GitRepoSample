package com.example.gitrepos

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.gitrepos.TestUtility.Companion.getTestGitRepoData
import com.example.gitrepos.data.*
import com.example.gitrepos.data.Status.Loading
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


    private lateinit var gitRepoViewModel: GitRepoViewModel

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
        val gitRepoDataLoading = liveDataUnderTest?.observedValues?.get(0) as Status
        assertTrue(gitRepoDataLoading == Loading)
        assertFalse(gitRepoDataLoading is Status.Success)
        assertFalse(gitRepoDataLoading is Status.Error)
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
        val dataSuccess = liveDataUnderTest?.observedValues?.get(1) as Status
        assertTrue(dataSuccess is Status.Success)
        assertEquals((dataSuccess as Status.Success).gitData, testGitRepoData)
        assertFalse(dataSuccess is Status.Error)
        assertFalse(dataSuccess == Loading)
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
        val dataError = liveDataUnderTest?.observedValues?.get(1) as Status
        assertTrue(dataError is Status.Error)
        assertFalse(dataError is Status.Success)
        assertFalse(dataError == Loading)
        assertNotNull((dataError as Status.Error).error.errorMessage)
        assertEquals(dataError.error.errorMessage, Constants.ERROR_MESSAGE)
    }


    @Test
    fun testViewModelInitialState() {

        val liveData = gitRepoViewModel.getGitRepoData()
        val data = liveData?.value
        assertFalse(data is Status.Error)
        assertFalse(data is Status.Success)
        assertFalse(data === Loading)
    }

}

