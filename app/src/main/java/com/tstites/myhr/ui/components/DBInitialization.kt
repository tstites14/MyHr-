package com.tstites.myhr.ui.components

import com.tstites.myhr.obj.*

class DBInitialization {
    fun init(ed: EmployeeDao,
               pd: ProjectDao,
               ped: ProjectEmployeeDao,
               cd: CustomerDao,
               cpd: CustomerProjectDao) {
        insertData(ed)
        insertData(pd)
        insertData(ped)
        insertData(cd)
        insertData(cpd)
    }

    private fun insertData(ed: EmployeeDao) {
        if (ed.getTableEntries() == 0) {
            val e1 = Employee(1, "John Doe", "12 Test Ave", "Orlando", "FL",
                "Information Technology", "Junior Software Engineer", 100)
            val e2 = Employee(2, "James Phillips", "432 Sample Ave", "Kissimmee", "FL",
                "Information Technology", "Software Engineer", 101)
            val e3 = Employee(3, "Sophia Wright", "641 QA Lane", "Orlando", "FL",
                "Marketing", "Junior Social Media Manager", 102)

            ed.insertNewEmployee(e1, e2, e3)
        }
    }

    private fun insertData(pd: ProjectDao) {
        if (pd.getTableEntries() == 0) {
            val p1 = Project(0, "MyHr+", 0f)
            val p2 = Project(0, "Docx to Pdf Converter", 0f)
            val p3 = Project(0, "Project Jupiter", 0f)
            val p4 = Project(0, "Source", 0f)
            val p5 = Project(0, "Meridian", 0f)

            pd.insertNewProject(p1, p2, p3, p4, p5)
        }
    }

    private fun insertData(ped: ProjectEmployeeDao) {
        if (ped.getTableEntries() == 0) {
            val pe1 = ProjectEmployee(1, 1, 0.33f)
            val pe2 = ProjectEmployee(2, 1, 0.5f)
            val pe3 = ProjectEmployee(1, 2, 0f)

            ped.insertNewProjectEmployee(pe1, pe2, pe3)
        }
    }

    private fun insertData(cd: CustomerDao) {
        if (cd.getTableEntries() == 0) {
            val c1 = Customer(0, "Alphatronics", "807 Rockaway St.", "Chicago", "IL", "7735025088", "dev@alphatronics.com")
            val c2 = Customer(0, "Apexi Co.", "906 Albany St.", "San Francisco", "CA", "415251-3415", "dev@apexi.com")
            val c3 = Customer(0, "Vertex Corporation", "647 Chestnut Lane", "New York", "NY", "646463-9275", "dev@vertexco.com")
            val c4 = Customer(0, "ImageWorks Systems", "810 Woodsman St.", "Orlando", "FL", "407852-6028", "dev@imageworks.com")

            cd.insertNew(c1, c2, c3, c4)
        }
    }

    private fun insertData(cpd: CustomerProjectDao) {
        if (cpd.getTableEntries() == 0) {
            val cp1 = CustomerProject(1, 2)
            val cp2 = CustomerProject(2, 3)
            val cp3 = CustomerProject(3, 4)
            val cp4 = CustomerProject(4, 5)

            cpd.insertNew(cp1, cp2, cp3, cp4)
        }
    }
}