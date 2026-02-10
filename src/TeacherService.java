import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    public static Teacher addTeacher(String name) {
        int id = EnglishSchool.teachers.size() + 1;
        Teacher t = new Teacher(id, name);
        EnglishSchool.teachers.add(t);
        return t;
    }

    public static List<Teacher> getTeachers() {
        return new ArrayList<>(EnglishSchool.teachers);
    }

    public static Teacher findTeacher(int teacherId) {
        return Student.findTeacher(teacherId);
    }
}
