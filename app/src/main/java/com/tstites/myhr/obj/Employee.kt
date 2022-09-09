package com.tstites.myhr.obj

import androidx.room.*

/*Android Room uses this class to store data in the database.
* The data class stores the data like a normal class but also contains an annotation that
* determines the column name in the database. This means that any time the Employee object is
* called, the data given to it in its constructor will be associated with that DB column*/
@Entity
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "address") var address: String?,
    @ColumnInfo(name = "city") var city: String?,
    @ColumnInfo(name = "state") var state: String?,
    @ColumnInfo(name = "department") var department: String?,
    @ColumnInfo(name = "jobTitle") var jobTitle: String?,
    @ColumnInfo(name = "extension") var phoneExtension: Int?
)

/*Android Room uses a DAO (Data Access Object) to provide access to the database. It is an interface
* that provides the function names and what query they will use when called. The @Query annotation
* may be any SQL command that the developer wishes (select, delete, update, script, etc.), but
* Android Room provides other boilerplate ones such as @Insert, @Delete, and @Update to perform basic
* SQL functions. These operate by passing the table's object (in this case Employee) to the DAO
* where, since it knows what field corresponds to what column, it performs the required operation based
* on the given object*/
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