package com.ajdev.vnzlaapp.ui.screens.exchangescreen

import com.ajdev.vnzlaapp.domain.model.Datetime
import com.ajdev.vnzlaapp.domain.model.Exchange
import com.ajdev.vnzlaapp.domain.model.ExchangeCompany
import com.ajdev.vnzlaapp.domain.model.Monitors
import com.ajdev.vnzlaapp.domain.usecase.GetExchangeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeViewModelTest {

    @RelaxedMockK
    private lateinit var getExchangeUseCase: GetExchangeUseCase

    @InjectMockKs
    private lateinit var viewModel: ExchangeViewModel

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
    fun `fetchData sets exchange value on success`() = runTest {
        val fakeExchange = Exchange(
            datetime = Datetime("", ""),
            monitors = Monitors(
                bcv = ExchangeCompany(image = "", price = 36.0),
                enparalelovzla = ExchangeCompany(image = "", price = 37.0)
            )
        )
        coEvery { getExchangeUseCase() } returns fakeExchange
        viewModel.fetchData()
        advanceUntilIdle()
        assertEquals(fakeExchange, viewModel.exchange.value)
        assertFalse(viewModel.errorState.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `fetchData sets exchange value on error`() = runTest {
        coEvery { getExchangeUseCase() } throws RuntimeException()
        viewModel.fetchData()
        advanceUntilIdle()
        assertTrue(viewModel.errorState.value)
    }

    @Test
    fun `calculateAmount should update calculatedAmounts correctly when is dollar to bs multiply`() = runTest {
        val fakeExchange = Exchange(
            datetime = Datetime("", ""),
            monitors = Monitors(
                bcv = ExchangeCompany(image = "", price = 36.0),
                enparalelovzla = ExchangeCompany(image = "", price = 37.0)
            )
        )
        coEvery { getExchangeUseCase() } returns fakeExchange
        viewModel.fetchData()
        advanceUntilIdle()

        viewModel.setAmount("10")
        viewModel.setExchange(ChangeType.DOLLARTOBS)
        viewModel.calculateAmount()

        val result = viewModel.calculatedAmounts.value
        assert(result.bcv.contains("360"))
        assert(result.parallel.contains("370"))
        assert(result.average.contains("365"))
    }

    @Test
    fun `calculateAmount should update calculatedAmounts correctly when is bs to dollar divide`() = runTest {
        val fakeExchange = Exchange(
            datetime = Datetime("", ""),
            monitors = Monitors(
                bcv = ExchangeCompany(image = "", price = 80.0),
                enparalelovzla = ExchangeCompany(image = "", price = 100.0)
            )
        )
        coEvery { getExchangeUseCase() } returns fakeExchange
        viewModel.fetchData()
        advanceUntilIdle()

        viewModel.setAmount("300")
        viewModel.setExchange(ChangeType.BSTODOLLAR)
        viewModel.calculateAmount()

        val result = viewModel.calculatedAmounts.value
        assert(result.bcv.contains("3,75"))
        assert(result.parallel.contains("3"))
        assert(result.average.contains("3,33"))
    }
}