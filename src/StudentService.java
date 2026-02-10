import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    public static Student addStudent(String name, int age, String sex, long phone, String email, String course) {
        int id = EnglishSchool.students.size() + 1;
        Student s = new Student(
                id, name, age, sex,
                phone, email, course,
                0, "在籍", "", ""
        );
        s.setRegisterDate(LocalDate.now());
        EnglishSchool.students.add(s);
        return s;
    }

    public static List<Student> getActiveStudents() {
        List<Student> result = new ArrayList<>();
        for (Student s : EnglishSchool.students) {
            if ("在籍".equals(s.getStatus())) {
                result.add(s);
            }
        }
        return result;
    }

    public static Student findStudent(int studentId) {
        for (Student s : EnglishSchool.students) {
            if (s.getId() == studentId) {
                return s;
            }
        }
        return null;
    }

    public static ActionResult validateActiveStudent(int studentId) {
        Student s = findStudent(studentId);
        if (s == null) {
            return ActionResult.failure("生徒が見つかりません。");
        }
        if (!"在籍".equals(s.getStatus())) {
            return ActionResult.failure("在籍中の生徒が見つかりません。");
        }
        return ActionResult.success("");
    }

    public static ActionResult changeStudentCourse(int studentId, String course) {
        Student s = findStudent(studentId);
        if (s == null) {
            return ActionResult.failure("生徒が見つかりません。");
        }
        if (!"在籍".equals(s.getStatus())) {
            return ActionResult.failure("在籍中の生徒が見つかりません。");
        }
        String before = s.getCourse();
        s.setCourse(course);
        return ActionResult.success("****コース変更****\n変更前: " + before + "\n変更後: " + course);
    }

    public static ActionResult removeStudent(int studentId) {
        Student s = findStudent(studentId);
        if (s == null) {
            return ActionResult.failure("生徒が見つかりません。");
        }
        if (!"在籍".equals(s.getStatus())) {
            return ActionResult.failure("すでに退学済みです。");
        }
        s.setStatus("退学");
        return ActionResult.success("退学処理が完了しました。");
    }

    public static ActionResult addPoints(int studentId, int points) {
        Student s = findStudent(studentId);
        if (s == null) {
            return ActionResult.failure("生徒が見つかりません。");
        }
        if (!"在籍".equals(s.getStatus())) {
            return ActionResult.failure("在籍中の生徒が見つかりません。");
        }
        if (s.addPoints(points)) {
            return ActionResult.success("ポイント追加完了");
        }
        return ActionResult.failure("200単位で入力してください");
    }
}
