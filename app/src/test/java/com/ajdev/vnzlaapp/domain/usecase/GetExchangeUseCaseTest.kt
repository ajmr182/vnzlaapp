package com.ajdev.vnzlaapp.domain.usecase

import com.ajdev.vnzlaapp.data.model.Datetime
import com.ajdev.vnzlaapp.data.model.ExchangeCompany
import com.ajdev.vnzlaapp.data.model.ExchangeResponse
import com.ajdev.vnzlaapp.data.model.Monitors
import com.ajdev.vnzlaapp.domain.mapper.toDomain
import com.ajdev.vnzlaapp.domain.repository.ExchangeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetExchangeUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: ExchangeRepository

    @InjectMockKs
    private lateinit var useCase: GetExchangeUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return exchange from repository`() = runTest {
        val exchangeDto = ExchangeResponse(datetime = Datetime(date = "2", time = "1"), monitors = Monitors(
            bcv = ExchangeCompany(image = "asd", 4.00), enparalelovzla = ExchangeCompany(image = "asdw", price = 5.03)
        ))
        val expectedExchange = exchangeDto.toDomain()

        coEvery { repository.getExchange() } returns exchangeDto

        val result = useCase()

        assertEquals(expectedExchange, result)
        coVerify(exactly = 1) { repository.getExchange() }
    }
}