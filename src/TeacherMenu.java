public class TeacherMenu {
    public static void showMenu() {
        while (true) {
            try {
                System.out.println(Main.LINE);
                System.out.print("""
                        
                        1: レッスン一覧
                        2: 出席登録
                        0: 戻る
                        
                        番号を入力してください>>> """);
                int choice = Integer.parseInt(Main.sc.nextLine());
                switch (choice) {
                    case 1 -> viewLessons();
                    case 2 -> recordAttendance();
                    case 0 -> { return; }
                    default -> System.out.println("無効な入力です。");
                }
            } catch (NumberFormatException e) {
                System.out.println("数字を入力してください。");
            }
        }
    }

    public static void viewLessons() {
        System.out.println(Main.LINE);
        System.out.print("講師ID: ");
        int teacherId = Integer.parseInt(Main.sc.nextLine());

        boolean found = false;
        for (Lesson l : Main.lessons) {
            if (l.getTeacherId() == teacherId && !"取消".equals(l.getStatus())) {
                System.out.println(
                        "レッスンID=" + l.getLessonId() +
                                " 生徒ID=" + l.getStudentId() +
                                " コース=" + l.getCourse() +
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
        System.out.println(Main.LINE);
        System.out.print("レッスンID: ");
        int lessonId = Integer.parseInt(Main.sc.nextLine());

        for (Lesson l : Main.lessons) {
            if (l.getLessonId() == lessonId) {
                Student student = findStudent(l.getStudentId());
                if (student != null) {
                    String summary = l.getDateTime() + " " + l.getCourse();
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
