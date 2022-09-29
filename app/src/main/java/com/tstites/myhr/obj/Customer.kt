package com.tstites.myhr.obj

import androidx.room.*

@Entity
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "address") var address: String?,
    @ColumnInfo(name = "city") var city: String?,
    @ColumnInfo(name = "state") var state: String?,
)

@Dao
interface CustomerDao {
    @Query("SELECT * FROM Customer WHERE id = :id")
    fun selectById(id: Int): Customer

    @Query("SELECT * FROM Customer WHERE name = :name")
    fun selectByName(name: String): Customer

    @Query("SELECT * FROM Customer WHERE state = :state")
    fun selectByState(state: String): List<Customer>

    @Insert
    fun insertNew(customer: Customer)

    @Delete
    fun deleteExisting(customer: Customer)
}

@Entity(primaryKeys = ["customerID", "projectID"])
data class CustomerProject(
    @ColumnInfo(name = "customerID") val customerID: Int,
    @ColumnInfo(name = "projectID") val projectID: Int
)

@Dao
interface CustomerProjectDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM CustomerProject JOIN Customer ON CustomerProject.customerID = Customer.id WHERE CustomerProject.customerID = :id")
    fun selectByCustomerId(id: Int): List<Customer>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM CustomerProject JOIN Project ON CustomerProject.projectID = Project.id WHERE CustomerProject.projectID = :id")
    fun selectByProjectId(id: Int): List<Customer>

    @Insert
    fun insertNew(customerProject: CustomerProject)

    @Delete
    fun deleteExisting(customerProject: CustomerProject)
}