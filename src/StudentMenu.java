import java.time.LocalDateTime;

public class StudentMenu {
    public static void showMenu() {
        System.out.println(Design.LINE);
        System.out.print("生徒ID: ");
        int studentId;
        try {
            studentId = Integer.parseInt(EnglishSchool.sc.nextLine());
        } catch (Exception e) {
            System.out.println("数字を入力してください！！！");
            return;
        }
        Student currentStudent = StaffMenu.findStudent(studentId);
        if (currentStudent == null || !"在籍".equals(currentStudent.getStatus())) {
            System.out.println("在籍中の生徒が見つかりません");
            return;
        }

        while (true) {
            Design.clearScreen();
            System.out.println(Design.LINE);
            System.out.println("生徒: " + currentStudent.getName() + " (ID: " + studentId + ")");
            System.out.print("""

                        1: 生徒情報確認
                        2: レッスン予約
                        3: レッスン確認
                        0: 戻る

                        """);
            while (true) {
                System.out.print("番号を入力してください>>> ");
                try {
                    int choice = Integer.parseInt(EnglishSchool.sc.nextLine());
                    switch (choice) {
                        case 1 -> viewStudent(studentId);
                        case 2 -> reserveLesson(studentId);
                        case 3 -> viewLessons(studentId);
                        case 0 -> { return; }
                        default -> {
                            System.out.println("無効な入力です。");
                            continue;
                        }
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("数字を入力してください。");
                }
            }
        }
    }

    public static void viewStudent(int studentId) {
        System.out.println(Design.LINE);
        for (Student s : EnglishSchool.students) {
            if (s.getId() == studentId) {
                System.out.println(
                        "ID=" + s.getId() +
                                " 名前=" + s.getName() +
                                " コース=" + s.getCourse() +
                                " ポイント=" + s.getPoints() +
                                " レッスン=" + s.getLesson() +
                                " 登録日=" + s.getRegisterDate()
                );
                return;
            }
        }
        System.out.println("生徒が見つかりません");
    }

    public static void reserveLesson(int studentId) {
        System.out.println(Design.LINE);

        int lessonId = EnglishSchool.lessons.size()+1;
        Student student = StaffMenu.findStudent(studentId);
        System.out.print("講師ID: ");
        int teacherId = Integer.parseInt(EnglishSchool.sc.nextLine());

        String lessonType = CourseUtil.SelectLessonType();

        System.out.print("日時 (例: 2026-02-01 18): ");
        String input = EnglishSchool.sc.nextLine();
        LocalDateTime dateTime;
        try {
            dateTime = DateTimeUtil.parse(input);
        } catch (Exception e) {
            System.out.println("日時の形式が正しくありません。");
            return;
        }
        if (dateTime.isBefore(LocalDateTime.now())) {
            System.out.println("過去の日時は予約できません。");
            return;
        }

        String formattedDateTime = DateTimeUtil.format(dateTime);
        for(Lesson l : EnglishSchool.lessons){
            if(!"取消".equals(l.getStatus()) && formattedDateTime.equals(l.getDateTime())){
                if(l.getTeacherId() == teacherId){
                    System.out.println("講師はその時間に予約があります。");
                    return;
                }
                if(l.getStudentId() == studentId){
                    System.out.println("生徒はその時間に予約があります。");
                    return;
                }
            }
        }

        if (student == null || !student.consumePoints(LessonCost.getLessonCost())) {
            System.out.println("ポイントが不足しています。");
            return;
        }

        Lesson l = new Lesson(lessonId, studentId, teacherId, lessonType, DateTimeUtil.format(dateTime));
        EnglishSchool.lessons.add(l);

        System.out.println("レッスンを予約しました。");
    }

    public static void viewLessons(int studentId) {
        System.out.println(Design.LINE);
        boolean found = false;
        for (Lesson l : EnglishSchool.lessons) {
            if (l.getStudentId() == studentId && !"取消".equals(l.getStatus())) {
                System.out.println(
                        "レッスンID=" + l.getLessonId() +
                                " 講師=" + Student.getTeacherName(l.getTeacherId()) +
                                " レッスンタイプ=" + l.getLessonType() +
                                " 日時=" + l.getDateTime() + "時"
                );
                found = true;
            }
        }
        if (!found) {
            System.out.println("レッスンがありません。");
        }
    }
}
