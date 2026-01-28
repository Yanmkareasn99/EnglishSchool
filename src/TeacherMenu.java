public class TeacherMenu {
    public static void showMenu() {
        while (true) {
            Design.clearScreen();
            System.out.println(Design.LINE);
            System.out.print("""
                        
                        1: レッスン一覧
                        2: 出席登録
                        0: 戻る
                        
                        """);
            while (true) {
                System.out.print("番号を入力してください>>> ");
                try {
                    int choice = Integer.parseInt(EnglishSchool.sc.nextLine());
                    switch (choice) {
                        case 1 -> viewLessons();
                        case 2 -> recordAttendance();
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

    public static void viewLessons() {
        System.out.println(Design.LINE);
        System.out.print("講師ID: ");
        int teacherId = Integer.parseInt(EnglishSchool.sc.nextLine());

        boolean found = false;
        for (Lesson l : EnglishSchool.lessons) {
            if (l.getTeacherId() == teacherId && !"取消".equals(l.getStatus())) {
                System.out.println(
                        "レッスンID=" + l.getLessonId() +
                                " 生徒ID=" + l.getStudentId() +
                                " レッスンタイプ=" + l.getLessonType() +
                                " 日時=" + l.getDateTime()
                );
                found = true;
            }
        }
        if (!found) {
            System.out.println("該当レッスンがありません。");
        }
    }

    public static void recordAttendance() {
        System.out.println(Design.LINE);
        System.out.print("レッスンID: ");
        int lessonId = Integer.parseInt(EnglishSchool.sc.nextLine());

        for (Lesson l : EnglishSchool.lessons) {
            if (l.getLessonId() == lessonId) {
                Student student = findStudent(l.getStudentId());
                if (student != null) {
                    String summary = l.getDateTime() + " " + l.getLessonType();
                    student.setLesson(summary);
                    System.out.println("出席を登録しました。");
                } else {
                    System.out.println("生徒が見つかりません。");
                }
                return;
            }
        }
        System.out.println("レッスンが見つかりません。");
    }

    public static Student findStudent(int studentId) {
        return StaffMenu.findStudent(studentId);
    }
}
