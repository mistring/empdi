package net.mistring.empdi.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.mistring.empdi.data.EmployeeRepository
import net.mistring.empdi.model.EmployeeEntry
import javax.inject.Inject

/**
 * The ViewModel:
 * - handles requests from the UI (View: Activity, Fragment, etc)
 * - prepares data from the Repository for UI (View) consumption
 * This ViewModel can be shared between MainActivity and its Fragments
 */
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: EmployeeRepository
) : ViewModel() {

    private var filterType = EmployeeFilter.UNFILTERED

    // Backing property to avoid state updates from other classes
    private val _loaderStateFlow = MutableStateFlow(false)

    // The UI collects from this StateFlow to get its state updates
    val loaderStateFlow = _loaderStateFlow.asStateFlow()

    private val _uiState = MutableStateFlow<EmployeeUiState>(EmployeeUiState.Success(emptyList()))
    val uiState: StateFlow<EmployeeUiState> = _uiState

    fun fetchEmployees(type: EmployeeFilter = EmployeeFilter.UNFILTERED) {

        filterType = type

        // Signal that the Loading should be visible
        _loaderStateFlow.value = true

        viewModelScope.launch {
            repository.getEmployeeDirectory(filterType)
                .onCompletion {
                    // Signal that the Loading should disappear now
                    _loaderStateFlow.value = false
                }
                .collectLatest { result ->
                    if (result.isSuccess) {
                        result.getOrNull()?.let { wrapper ->
                            _uiState.value = EmployeeUiState.Success(wrapper.employees)
                        }
                    } else {
                        //handle failure in UI as necessary
                        _uiState.value = EmployeeUiState.Error(result.exceptionOrNull())
                    }
                }
        }

    }

    fun unfilteredRefresh() {
        fetchEmployees(EmployeeFilter.UNFILTERED)
    }

    fun sortByName() {
        fetchEmployees(EmployeeFilter.NAME)
    }

    fun sortByTeam() {
        fetchEmployees(EmployeeFilter.TEAM)
    }

    fun fetchMalformedList() {
        fetchEmployees(EmployeeFilter.MALFORMED)
    }

    fun fetchEmptyList() {
        fetchEmployees(EmployeeFilter.EMPTY)
    }

    fun callBadURL() {
        fetchEmployees(EmployeeFilter.BAD_RUL)
    }

    companion object {
        // Available Employee Directory Filter options
        enum class EmployeeFilter {
            NAME, TEAM, UNFILTERED, MALFORMED, EMPTY, BAD_RUL
        }
    }

}

sealed class EmployeeUiState {
    data class Success(val employees: List<EmployeeEntry>) : EmployeeUiState()
    data class Error(val exception: Throwable?) : EmployeeUiState()
}