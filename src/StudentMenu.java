public class StudentMenu {
    public static void showMenu() {
        while (true) {
            try {
                System.out.println(Main.LINE);
                System.out.print("""
                        
                        1: 生徒情報確認
                        2: レッスン予約
                        3: レッスン確認
                        0: 戻る
                        
                        番号を入力してください>>> """);
                int choice = Integer.parseInt(Main.sc.nextLine());
                switch (choice) {
                    case 1 -> viewStudent();
                    case 2 -> StaffMenu.reserveLesson();
                    case 3 -> viewLessons();
                    case 0 -> { return; }
                    default -> System.out.println("無効な入力です。");
                }
            } catch (NumberFormatException e) {
                System.out.println("数字を入力してください。");
            }
        }
    }

    public static void viewStudent() {
        System.out.println(Main.LINE);
        System.out.print("生徒ID: ");
        int id = Integer.parseInt(Main.sc.nextLine());

        for (Student s : Main.students) {
            if (s.getId() == id) {
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
        System.out.println("生徒が見つかりません。");
    }

    public static void viewLessons() {
        System.out.println(Main.LINE);
        System.out.print("生徒ID: ");
        int studentID = Integer.parseInt(Main.sc.nextLine());
        boolean found = false;
        for (Lesson l : Main.lessons) {
            if (l.getStudentId() == studentID && !"取消".equals(l.getStatus())) {
                System.out.println(
                        "レッスンID=" + l.getLessonId() +
                                " 講師ID=" + l.getTeacherId() +
                                " コース=" + l.getCourse() +
                                " 日時=" + l.getDateTime()
                );
                found = true;
            }
        }
        if (!found) {
            System.out.println("レッスンがありません。");
        }
    }

    public static Student findStudent(int studentId) {
        return StaffMenu.findStudent(studentId);
    }
}
