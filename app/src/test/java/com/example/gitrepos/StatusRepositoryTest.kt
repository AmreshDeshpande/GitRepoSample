package com.example.gitrepos

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.gitrepos.data.DataRepository
import com.example.gitrepos.network.GitRepoService
import com.example.gitrepos.network.model.GitRepo
import com.example.gitrepos.util.Constants
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class StatusRepositoryTest {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var gitRepoService: GitRepoService

    @Mock
    lateinit var getGitRepoCall: Call<List<GitRepo>?>


    lateinit var dataRepository: DataRepository

    @Before
    fun setUp() {
        dataRepository = DataRepository(gitRepoService)
    }


    @Test
    fun testDataRepoSuccess() {

        val lock = CountDownLatch(1)

        val testGitRepoData = TestUtility.getTestGitRepoData()

        whenever(getGitRepoCall.enqueue(any())).thenAnswer {
            (it.getArgument(0) as Callback<List<GitRepo>?>).onResponse(
                getGitRepoCall,
                Response.success(TestUtility.getTestGitRepoData())
            )
        }

        whenever(gitRepoService.getGitRepo()).thenReturn(getGitRepoCall)


        dataRepository.getGitRepo({
            assertEquals(it, testGitRepoData)
            assertEquals(testGitRepoData.size, 1)
            lock.countDown()
        },
            {
            })

        //Wait for lambda to complete
        lock.await()
    }


    @Test
    fun testDataRepoFailure() {

        val lock = CountDownLatch(1)

        whenever(getGitRepoCall.enqueue(any())).thenAnswer {
            (it.getArgument(0) as Callback<List<GitRepo>?>).onFailure(getGitRepoCall, Throwable())
        }

        whenever(gitRepoService.getGitRepo()).thenReturn(getGitRepoCall)


        dataRepository.getGitRepo({
        },
            {
                assertNotNull(it.errorMessage)
                assertEquals(it.errorMessage, Constants.ERROR_MESSAGE)
                lock.countDown()
            })

        //Wait for lambda to complete
        lock.await()

    }
}