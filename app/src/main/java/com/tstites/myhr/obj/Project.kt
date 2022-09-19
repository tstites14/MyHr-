package com.tstites.myhr.obj

import androidx.room.*

@Entity
data class Project (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "progress") var progress: Float = 0f,
)

@Entity(primaryKeys = ["employeeID", "projectID"] )
data class ProjectEmployee (
    val employeeID: Int,
    val projectID: Int,
    val currentProgress: Float
)

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Project")
    fun getAllProjects(): List<Project>

    @Query("SELECT * FROM Project WHERE id = :id")
    fun getProjectById(id: Int): Project

    @Query("SELECT * FROM Project WHERE title = :title")
    fun getProjectByTitle(title: String?): List<Project>

    @Query("SELECT * FROM Project WHERE progress >= :minCompletion")
    fun getProjectByMinCompletion(minCompletion: Float): List<Project>

    @Query("SELECT COUNT(*) FROM Project")
    fun getTableEntries(): Int

    @Insert
    fun insertNewProject(vararg project: Project)

    @Update
    fun updateExistingProject(project: Project)

    @Delete
    fun deleteProject(project: Project): Int
}

@Dao
interface ProjectEmployeeDao {
    @Query("SELECT * FROM ProjectEmployee")
    fun getAllProjectEmployees(): ProjectEmployee

    @Query("SELECT * FROM ProjectEmployee WHERE projectID = :projectID")
    fun getEmployeeIDsByProject(projectID: Int): List<ProjectEmployee>

    @Query("SELECT * FROM ProjectEmployee WHERE employeeID = :employeeID")
    fun getProjectIDsByEmployee(employeeID: Int): List<ProjectEmployee>

    @Insert
    fun insertNewProjectEmployee(vararg projectEmployee: ProjectEmployee)

    @Delete
    fun deleteProjectEmployee(projectEmployee: ProjectEmployee): Int
}