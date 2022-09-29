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

    @Query("DELETE FROM Project WHERE id = :id")
    fun deleteProjectByID(id: Int): Int

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
    fun getAllProjectEmployees(): List<ProjectEmployee>

    @Query("SELECT * FROM ProjectEmployee WHERE projectID = :projectID")
    fun getEmployeeIDsByProject(projectID: Int): List<ProjectEmployee>

    @Query("SELECT * FROM ProjectEmployee WHERE employeeID = :employeeID")
    fun getProjectIDsByEmployee(employeeID: Int): List<ProjectEmployee>

    @Query("SELECT * FROM ProjectEmployee WHERE employeeID = :employeeID AND projectID = :projectID")
    fun getSpecificProjectEmployee(employeeID: Int, projectID: Int): ProjectEmployee

    @Query("DELETE FROM ProjectEmployee WHERE employeeID = :employeeID AND projectID = :projectID")
    fun deleteProjectEmployeeByID(employeeID: Int, projectID: Int): Int

    @Query("SELECT AVG(currentProgress) AS 'average' FROM ProjectEmployee WHERE projectID = :projectID")
    fun getProgressAverageByProject(projectID: Int): Float

    @Query("SELECT COUNT(*) FROM ProjectEmployee")
    fun getTableEntries(): Int

    @Insert
    fun insertNewProjectEmployee(vararg projectEmployee: ProjectEmployee)

    @Update
    fun updateExistingProjectEmployee(projectEmployee: ProjectEmployee)

    @Delete
    fun deleteProjectEmployee(projectEmployee: ProjectEmployee): Int
}