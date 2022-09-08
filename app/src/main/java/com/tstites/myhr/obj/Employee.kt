package com.tstites.myhr.obj

import androidx.room.*

@Entity
data class Employee(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "address") var address: String?,
    @ColumnInfo(name = "city") var city: String?,
    @ColumnInfo(name = "state") var state: String?,
    @ColumnInfo(name = "department") var department: String?,
    @ColumnInfo(name = "jobTitle") var jobTitle: String?,
    @ColumnInfo(name = "extension") var phoneExtension: Int?
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

    @Update
    fun updateExistingEmployee(employee: Employee)

    @Delete
    fun deleteEmployee(employee: Employee): Int
}