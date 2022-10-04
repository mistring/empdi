package net.mistring.empdi.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.mistring.empdi.model.EmployeesWrapper
import net.mistring.empdi.model.UserDatabase
import net.mistring.empdi.view.SharedViewModel
import net.mistring.empdi.view.SharedViewModel.Companion.EmployeeFilter.NAME
import net.mistring.empdi.view.SharedViewModel.Companion.EmployeeFilter.TEAM
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
    private val api: EmployeeAPI,
    private val db: UserDatabase
) {

    suspend fun getEmployeeDirectory(filter: SharedViewModel.Companion.EmployeeFilter): Flow<Result<EmployeesWrapper>> {
        return flow {
            try {

                // Determine the endpoint
                val employeesWrapper = when (filter) {
                    SharedViewModel.Companion.EmployeeFilter.MALFORMED -> {
                        Timber.d("Calling \"malformed\" endpoint")
                        api.getEmployeesMalformed()
                    }
                    SharedViewModel.Companion.EmployeeFilter.EMPTY -> {
                        Timber.d("Calling \"empty\" endpoint")
                        api.getEmployeesEmpty()
                    }
                    SharedViewModel.Companion.EmployeeFilter.BAD_RUL -> {
                        Timber.d("Calling non-existent endpoint")
                        api.getEmployeesBadUrl()
                    }
                    else -> {
                        Timber.d("Calling base endpoint")

                        val userCount = db.userDao().userCount()
                        if (userCount > 0) {
                            Timber.d("Calling base endpoint DB")
                            EmployeesWrapper(db.userDao().getUsers())
                        } else {
                            Timber.d("Calling base endpoint NET")
                            api.getEmployees()
                        }

                    }

                }

                if (employeesWrapper.isValid()) {

                    employeesWrapper.employees.forEach { employee ->
                        db.userDao().insertUser(employee)
                    }

                    val userCount = db.userDao().userCount()
                    Timber.d("userCount=$userCount")

                    // Apply sorting, if needed
                    if (filter == NAME) {
                        employeesWrapper.employees =
                            employeesWrapper.employees.sortedBy { it.fullName }
                    } else if (filter == TEAM) {
                        employeesWrapper.employees = employeesWrapper.employees.sortedBy { it.team }
                    }

                    emit(Result.success(employeesWrapper))
                } else {
                    Timber.w("Invalid employee data detected")
                    emit(Result.failure(throw Exception("Malformed Employee Directory")))
                }


            } catch (e: Exception) {
                Timber.e("API failure: $e")
                emit(Result.failure(e))
            }
        }
    }
}