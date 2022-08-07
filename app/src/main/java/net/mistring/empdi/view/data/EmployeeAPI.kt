package net.mistring.empdi.view.data

import com.vivint.coroutines_sample.model.EmployeesWrapper
import retrofit2.http.GET

interface EmployeeAPI {

    @GET("employees.json")
    suspend fun getEmployees(): EmployeesWrapper

    @GET("employees_malformed.json")
    suspend fun getEmployeesMalformed(): EmployeesWrapper

    @GET("employees_empty.json")
    suspend fun getEmployeesEmpty(): EmployeesWrapper

}