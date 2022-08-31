package com.tstites.myhr.obj

import androidx.room.*

@Entity
data class Employee(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "department") val department: String?,
    @ColumnInfo(name = "jobTitle") val jobTitle: String?,
    @ColumnInfo(name = "extension") val phoneExtension: Int?
)

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM Employee")
    fun getAllEmployees(): List<Employee>

    @Query("SELECT * FROM Employee WHERE id = :id")
    fun getEmployeeById(id: Int): Employee

    @Query("SELECT * FROM Employee WHERE department = :department")
    fun getEmployeesFromDepartment(department: String): List<Employee>

    @Query("SELECT COUNT(*) FROM Employee")
    fun getTableEntries(): Int

    @Insert
    fun insertNewEmployee(vararg employee: Employee)

    @Delete
    fun deleteEmployee(employee: Employee): Int
}