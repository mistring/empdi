package net.mistring.empdi.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class EmployeesWrapper(
    var employees: List<EmployeeEntry>
) {

    fun isValid() = employees.fold(0) { acc, e ->
        if (e.uuid.isEmpty() ||
            e.fullName.isEmpty() ||
            e.emailAddress.isEmpty() ||
            e.team.isEmpty() ||
            e.employeeType == EmployeeType.UNKNOWN
        ) acc + 1 else acc
    } == 0

}

@Entity
data class EmployeeEntry(

    @PrimaryKey
    val uuid: String = "",

    @SerializedName("full_name")
    val fullName: String = "",

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("email_address")
    val emailAddress: String = "",

    val biography: String? = null,

    @SerializedName("photo_url_small")
    val photoUrlSmall: String? = null,

    @SerializedName("photo_url_large")
    val photoUrlLarge: String? = null,

    val team: String = "",

    @SerializedName("employee_type")
    val employeeType: EmployeeType = EmployeeType.UNKNOWN
) {
    override fun toString(): String {
        return "EmployeeEntry(uuid='$uuid', fullName='$fullName', phoneNumber=$phoneNumber, emailAddress='$emailAddress', biography=$biography, photoUrlSmall=$photoUrlSmall, photoUrlLarge=$photoUrlLarge, team='$team', employeeType=$employeeType)"
    }
}

enum class EmployeeType {
    @SerializedName("FULL_TIME")
    FULL_TIME,

    @SerializedName("PART_TIME")
    PART_TIME,

    @SerializedName("CONTRACTOR")
    CONTRACTOR,

    UNKNOWN
}

/*

Employee List: https://s3.amazonaws.com/sq-mobile-interview/employees.json
Error List: https://s3.amazonaws.com/sq-mobile-interview/employees_malformed.json
Empty List: https://s3.amazonaws.com/sq-mobile-interview/employees_empty.json

{
  "uuid": "0d8fcc12-4d0c-425c-8355-390b312b909c",
  "full_name": "Justine Mason",
  "phone_number": "5553280123",
  "email_address": "jmason.demo@squareup.com",
  "biography": "Engineer on the Point of Sale team.",
  "photo_url_small": "https://s3.amazonaws.com/sq-mobile-interview/photos/16c00560-6dd3-4af4-97a6-d4754e7f2394/small.jpg",
  "photo_url_large": "https://s3.amazonaws.com/sq-mobile-interview/photos/16c00560-6dd3-4af4-97a6-d4754e7f2394/large.jpg",
  "team": "Point of Sale",
  "employee_type": "FULL_TIME"
},

   */