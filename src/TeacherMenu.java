public class TeacherMenu {
    public static void showMenu() {
        int teacherId;
        Design.clearScreen();
        System.out.println(Design.LINE);
        while (true) {
            System.out.print("講師ID: ");
            try {
                teacherId = Integer.parseInt(EnglishSchool.sc.nextLine());
            } catch (Exception e) {
                System.out.println("数字を入力してください！！！");
                continue;
            }
            Teacher currentTeacher = TeacherService.findTeacher(teacherId);
            if (currentTeacher == null) {
                System.out.println("先生が見つかりません");
                continue;
            }
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
                            case 1 -> viewLessons(teacherId);
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
    }


    public static void viewLessons(int teacherId) {
        System.out.println(Design.LINE);

        boolean found = false;
        for (Lesson l : LessonService.getLessonsForTeacher(teacherId)) {
            System.out.println(
                    "レッスンID=" + l.getLessonId() +
                            " 生徒ID=" + l.getStudentId() +
                            " レッスンタイプ=" + l.getLessonType() +
                            " 日時=" + l.getDateTime()
            );
            found = true;
        }
        if (!found) {
            System.out.println("該当レッスンがありません。");
        }
    }

    public static void recordAttendance() {
        System.out.println(Design.LINE);
        System.out.print("レッスンID: ");
        int lessonId = Integer.parseInt(EnglishSchool.sc.nextLine());

        ActionResult result = LessonService.recordAttendance(lessonId);
        System.out.println(result.getMessage());
    }

}
