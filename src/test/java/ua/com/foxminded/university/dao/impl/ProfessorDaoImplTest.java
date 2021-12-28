package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dao.interfaces.RoleDao;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.ScienceDegree;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsers;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsersProfessors;

class ProfessorDaoImplTest {

    ApplicationContext context;
    ProfessorDao professorDao;
    DepartmentDao departmentDao;
    RoleDao roleDao;
    Department departmentForTest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        professorDao = context.getBean(ProfessorDaoImpl.class);
        departmentDao = context.getBean(DepartmentDaoImpl.class);
        roleDao = context.getBean(RoleDaoImpl.class);
        departmentForTest = Department.builder().withId(0L).build();
    }

    @Test
    void createAndReadShouldAddNewProfessorToDatabaseIfArgumentIsProfessor(){
        Professor addingProfessor = Professor.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentForTest)
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build();
        professorDao.save(addingProfessor);
        Professor readingProfessor = professorDao.findById(11L).get();

        assertUsers(readingProfessor, addingProfessor);
    }

    @Test
    void createAndReadShouldAddListOfNewProfessorToDatabaseIfArgumentIsListOfProfessors(){
        List<Professor> addingProfessorEntities = Arrays.asList (Professor.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentForTest)
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build());
        professorDao.saveAll(addingProfessorEntities);
        List<Professor> readingProfessorEntities = Arrays.asList(professorDao.findById(11L).get());

        assertUsersProfessors(readingProfessorEntities, addingProfessorEntities);
    }

    @Test
    void updateShouldUpdateDataOfProfessorIfArgumentIsProfessor(){
        Professor expected = Professor.builder()
                .withId(9L)
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById(1L).get())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build();
        professorDao.update(expected);
        Professor actual = professorDao.findById(9L).get();

        assertUsers(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfProfessorIfArgumentIsListOfProfessor(){
        List<Professor> expected = Arrays.asList(Professor.builder()
                .withId(9L)
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById(1L).get())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build());
        professorDao.updateAll(expected);
        List<Professor> actual = Arrays.asList(professorDao.findById(9L).get());

        assertUsersProfessors(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Professor> expected = Optional.empty();
        professorDao.deleteById(10L);
        Optional<Professor> actual = professorDao.findById(10L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByEmailShouldReturnOptionalOfProfessorEntityIfArgumentIsEmail(){
        Professor expected = Professor.builder()
                .withFirstName("Ivan")
                .withLastName("Petrov")
                .withEmail("petrov@gmail.com")
                .withPassword("RI")
                .withDepartment(departmentDao.findById(1L).get())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build();
        Professor actual = professorDao.findByEmail("petrov@gmail.com").get();

        assertUsers(actual, expected);
    }

    @Test
    void findByEmailShouldReturnOptionalEmptyIfArgumentIsEmailAndItDontExist(){
        Optional<Professor> expected = Optional.empty();
        Optional<Professor> actual = professorDao.findByEmail("unknownemail@gmail.com");

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeScienceDegreeShouldChangeScienceDegreeOfProfessorIfArgumentIsIdOfProfessorAndIdOfNewScienceDegree(){
        Professor expected = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById(1L).get())
                .withScienceDegree(ScienceDegree.MASTER)
                .build();
        professorDao.changeScienceDegree(10, 2);
        Professor actual = professorDao.findById(10L).get();

        assertUsers(actual, expected);
    }

    @Test
    void findByCourseIdShouldFindListOfProfessorsIfArgumentIsIdOfCourse(){
        List<Professor> expected = Arrays.asList(professorDao.findById(7L).get(),
                professorDao.findById(9L).get());
        List<Professor> actual = professorDao.findByCourseId(1);

        assertUsersProfessors(actual, expected);
    }

    @Test
    void findByDepartmentIdShouldFindListOfProfessorsIfArgumentIsIdOfDepartment(){
        List<Professor> expected = Arrays.asList(professorDao.findById(7L).get(),
                professorDao.findById(8L).get(), professorDao.findById(9L).get(), professorDao.findById(10L).get());
        List<Professor> actual = professorDao.findByDepartmentId(1);

        assertUsersProfessors(actual, expected);
    }

    @Test
    void removeDepartmentFromProfessorShouldRemoveDepartmentFromProfessorIfArgumentIsIdOfProfessor(){
        Professor expectedBeforeChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById(1L).get())
                .withScienceDegree(ScienceDegree.PH_D_CANDIDATE)
                .build();
        Professor actualBeforeChanging = professorDao.findById(10L).get();
        assertUsers(actualBeforeChanging, expectedBeforeChanging);

        professorDao.removeDepartmentFromProfessor(10L);

        Professor expectedAfterChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(Department.builder().withId(0L).build())
                .withScienceDegree(ScienceDegree.PH_D_CANDIDATE)
                .build();
        Professor actualAfterChanging = professorDao.findById(10L).get();
        assertUsers(actualAfterChanging, expectedAfterChanging);
    }

    @Test
    void changeDepartmentFromProfessorShouldChangeDepartmentOfProfessorIfArgumentIsIdOfProfessorAndIdOfNewDepartment(){
        Professor expectedBeforeChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById(1L).get())
                .withScienceDegree(ScienceDegree.PH_D_CANDIDATE)
                .build();
        Professor actualBeforeChanging = professorDao.findById(10L).get();
        assertUsers(actualBeforeChanging, expectedBeforeChanging);

        professorDao.changeDepartment(10L, 2L);

        Professor expectedAfterChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById(2L).get())
                .withScienceDegree(ScienceDegree.PH_D_CANDIDATE)
                .build();
        Professor actualAfterChanging = professorDao.findById(10L).get();
        assertUsers(actualAfterChanging, expectedAfterChanging);
    }

    @Test
    void addRoleToUserShouldAddRoleToProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){

        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(2L).withName("ROLE_PROFESSOR").build());
        List<Role> actualListOfRoleBeforeAdding = roleDao.findByUserId(7L);
        assertThat(actualListOfRoleBeforeAdding).isEqualTo(expectedListOfRoleBeforeAdding);

        professorDao.addRoleToUser(7L, 3L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList(Role.builder().withId(2L).withName("ROLE_PROFESSOR").build(),
                Role.builder().withId(3L).withName("ROLE_ADMIN").build());
        List<Role> actualListOfRoleAfterAdding = roleDao.findByUserId(7L);
        assertThat(actualListOfRoleAfterAdding).isEqualTo(expectedListOfRoleAfterAdding);
    }

    @Test
    void removeRoleFromUserShouldRemoveRoleFromProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){

        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(2L).withName("ROLE_PROFESSOR").build());
        List<Role> actualListOfRoleBeforeAdding = roleDao.findByUserId(7L);
        assertThat(actualListOfRoleBeforeAdding).isEqualTo(expectedListOfRoleBeforeAdding);

        professorDao.removeRoleFromUser(7L, 2L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList();
        List<Role> actualListOfRoleAfterAdding = roleDao.findByUserId(7L);
        assertThat(actualListOfRoleAfterAdding).isEqualTo(expectedListOfRoleAfterAdding);
    }

}
