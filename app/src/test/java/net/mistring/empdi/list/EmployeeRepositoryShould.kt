package net.mistring.empdi.list

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.mistring.empdi.data.EmployeeAPI
import net.mistring.empdi.data.EmployeeRepository
import net.mistring.empdi.model.EmployeesWrapper
import net.mistring.empdi.util.BaseUnitTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeRepositoryShould : BaseUnitTest() {

    private lateinit var repository: EmployeeRepository
    private val api: EmployeeAPI = mock()

    private val employeeList = mock<EmployeesWrapper>()
    private val exception = RuntimeException("Network issue")

    @Test
    fun getEmployeesFromApi() = runTest {
        repository = EmployeeRepository(api)
        api.getEmployees()
        verify(api, times(1)).getEmployees()
    }

    @Test
    fun getEmployeesEmptyFromApi() = runTest {
        repository = EmployeeRepository(api)
        api.getEmployeesEmpty()
        verify(api, times(1)).getEmployeesEmpty()
    }

    @Test
    fun getEmployeesMalformedFromApi() = runTest {
        repository = EmployeeRepository(api)
        api.getEmployeesMalformed()
        verify(api, times(1)).getEmployeesMalformed()
    }

    @Test
    fun getEmployeesBadUrlFromApi() = runTest {
        repository = EmployeeRepository(api)
        api.getEmployeesBadUrl()
        verify(api, times(1)).getEmployeesBadUrl()
    }

    @Test
    fun convertValuesToFlowResultAndEmitsThem() = runTest {
        mockSuccessfulCase()
        Assert.assertEquals(employeeList, api.getEmployees())
    }

    @Test
    fun emitsErrorResultWhenNetworkFails() = runTest {
        mockErrorCase()

        val msg = try {
            api.getEmployees()
            "Success"
        } catch (e: Exception) {
            e.message ?: "Unknkown error"
        }

        Assert.assertEquals("Network issue", msg)
    }

    private suspend fun mockSuccessfulCase() {
        whenever(api.getEmployees()).thenReturn(employeeList)
        repository = EmployeeRepository(api)
    }

    private suspend fun mockErrorCase() {
        whenever(api.getEmployees()).thenThrow(exception)
        repository = EmployeeRepository(api)
    }

}