package net.mistring.empdi.view.data

import com.vivint.coroutines_sample.model.EmployeeEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * The repository receives service data, and performs logical operations on the data as needed
 * Data Sources may include:
 * - Network
 * - File system
 * - Shared Preferences
 * - DataStore
 */
class EmployeeRepository @Inject constructor(
    private val api: EmployeeAPI
) {

    suspend fun getEmployeeDirectory(): Flow<Result<List<EmployeeEntry>>> {
        return flow {
            try {
                val employees = api.getEmployees().employees.sortedBy { it.fullName }
                emit(Result.success(employees))
            } catch (e: Exception) {
                Timber.e("API failure: $e")
                emit(Result.failure(e))
            }
        }
    }
}