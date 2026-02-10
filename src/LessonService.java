import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LessonService {
    public static ReserveLessonResult reserveLesson(int studentId, int teacherId, String lessonType,
                                                    LocalDateTime dateTime, boolean checkTeacherExists) {
        Student student = StudentService.findStudent(studentId);
        if (student == null || !"在籍".equals(student.getStatus())) {
            return ReserveLessonResult.failure("在籍中の生徒が見つかりません。");
        }
        if (checkTeacherExists && TeacherService.findTeacher(teacherId) == null) {
            return ReserveLessonResult.failure("先生が見つかりません");
        }
        if (dateTime.isBefore(LocalDateTime.now())) {
            return ReserveLessonResult.failure("過去の日時は予約できません。");
        }

        String formattedDateTime = DateTimeUtil.format(dateTime);
        for (Lesson l : EnglishSchool.lessons) {
            if (!"取消".equals(l.getStatus()) && formattedDateTime.equals(l.getDateTime())) {
                if (l.getTeacherId() == teacherId) {
                    return ReserveLessonResult.failure("講師はその時間に予約があります。");
                }
                if (l.getStudentId() == studentId) {
                    return ReserveLessonResult.failure("生徒はその時間に予約があります。");
                }
            }
        }

        if (!student.consumePoints(LessonCost.getLessonCost())) {
            return ReserveLessonResult.insufficientPoints("ポイントが不足しています。");
        }

        int lessonId = EnglishSchool.lessons.size() + 1;
        Lesson l = new Lesson(lessonId, studentId, teacherId, lessonType, formattedDateTime);
        EnglishSchool.lessons.add(l);
        return ReserveLessonResult.success("レッスンを予約しました。");
    }

    public static List<Lesson> getLessonsForStudent(int studentId) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : EnglishSchool.lessons) {
            if (l.getStudentId() == studentId && !"取消".equals(l.getStatus())) {
                result.add(l);
            }
        }
        return result;
    }

    public static List<Lesson> getLessonsForTeacher(int teacherId) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : EnglishSchool.lessons) {
            if (!"取消".equals(l.getStatus())) {
                result.add(l);
            }
        }
        return result;
    }

    public static List<Lesson> getActiveLessons() {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : EnglishSchool.lessons) {
            if (!"取消".equals(l.getStatus())) {
                result.add(l);
            }
        }
        return result;
    }

    public static ActionResult cancelLesson(int lessonId) {
        for (Lesson l : EnglishSchool.lessons) {
            if (l.getLessonId() == lessonId) {
                if ("取消".equals(l.getStatus())) {
                    return ActionResult.failure("すでに取消済みです。");
                }
                l.setStatus("取消");
                return ActionResult.success("レッスンを取消しました。");
            }
        }
        return ActionResult.failure("レッスンが見つかりません。");
    }

    public static ActionResult recordAttendance(int lessonId) {
        for (Lesson l : EnglishSchool.lessons) {
            if (l.getLessonId() == lessonId) {
                Student student = StudentService.findStudent(l.getStudentId());
                if (student != null) {
                    String summary = l.getDateTime() + " " + l.getLessonType();
                    student.setLesson(summary);
                    return ActionResult.success("出席を登録しました。");
                }
                return ActionResult.failure("生徒が見つかりません。");
            }
        }
        return ActionResult.failure("レッスンが見つかりません。");
    }
}
