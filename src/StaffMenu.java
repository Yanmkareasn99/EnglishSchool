//Staff have all the authorities except recordAttandance();
import java.time.LocalDateTime;

public class StaffMenu {

    public static void showMenu() {
        while (true) {
            Design.clearScreen();
            System.out.println(Design.LINE);
            System.out.print("""
                       
                        1: 生徒登録
                        2: 生徒一覧
                        3: 生徒情報変更
                        4: ポイント購入
                        5: レッスン予約
                        6: レッスン確認
                        7: レッスンキャンセル
                        8: レッスン単価
                        9: 講師登録
                        10: 講師一覧
                        11: 生徒退会
                        12: 売上確認
                        0: 戻る
                       """);
            while (true) {
                System.out.print("番号を入力してください>>> ");
                try {
                    int choice = Integer.parseInt(EnglishSchool.sc.nextLine());
                    switch (choice) {
                        case 1 -> addStudent();
                        case 2 -> viewStudents();
                        case 3 -> changeStudent();
                        case 4 -> addPoints();
                        case 5 -> reserveLesson();
                        case 6 -> viewLessons();
                        case 7 -> cancelLesson();
                        case 8 -> changeLessonCost();
                        case 9 -> addTeacher();
                        case 10 -> viewTeachers();
                        case 11 -> removeStudent();
                        case 12 -> viewProfit();
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

    //student

    public static void addStudent() {
        System.out.println(Design.LINE);
        System.out.print("名前: ");
        String name = EnglishSchool.sc.nextLine();

        System.out.print("年齢: ");
        int age = Integer.parseInt(EnglishSchool.sc.nextLine());

        System.out.print("性別: ");
        String sex = EnglishSchool.sc.nextLine();

        System.out.print("電話番号: ");
        long phone = Long.parseLong(EnglishSchool.sc.nextLine());

        System.out.print("Email: ");
        String email = EnglishSchool.sc.nextLine();

        String course = CourseUtil.selectCourse();

        StudentService.addStudent(name, age, sex, phone, email, course);
        System.out.println("生徒を登録しました。");
    }

    public static void viewStudents() {
        System.out.println(Design.LINE);
        System.out.println("\n------ 生徒一覧 ------");
        for (Student s : StudentService.getActiveStudents()) {
            System.out.println(
                    "ID=" + s.getId() +
                            " 名前=" + s.getName() +
                            " コース=" + s.getCourse() +
                            " ポイント=" + s.getPoints() +
                            " 登録日=" + s.getRegisterDate()
            );
        }
    }

    public static void changeStudent() {
        System.out.println(Design.LINE);
        System.out.print("変更する生徒ID: ");
        int id = Integer.parseInt(EnglishSchool.sc.nextLine());
        ActionResult validation = StudentService.validateActiveStudent(id);
        if (!validation.isSuccess()) {
            System.out.println(validation.getMessage());
            return;
        }
        System.out.print("新しいコース名: ");
        String course = EnglishSchool.sc.nextLine();
        ActionResult result = StudentService.changeStudentCourse(id, course);
        System.out.println(result.getMessage());
    }

    public static void removeStudent() {
        System.out.println(Design.LINE);
        System.out.print("退会する生徒ID: ");
        int id = Integer.parseInt(EnglishSchool.sc.nextLine());

        ActionResult result = StudentService.removeStudent(id);
        System.out.println(result.getMessage());
    }

    //lesson

    public static void reserveLesson() {
        System.out.println(Design.LINE);
        viewStudents();
        System.out.print("生徒ID: ");
        int studentId = Integer.parseInt(EnglishSchool.sc.nextLine());
        ActionResult studentValidation = StudentService.validateActiveStudent(studentId);
        if (!studentValidation.isSuccess()) {
            System.out.println(studentValidation.getMessage());
            return;
        }
        viewTeachers();

        System.out.print("講師ID: ");
        int teacherId = Integer.parseInt(EnglishSchool.sc.nextLine());
        if (TeacherService.findTeacher(teacherId) == null) {
            System.out.println("先生が見つかりません");
            return;
        }
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
        ReserveLessonResult result = LessonService.reserveLesson(
                studentId, teacherId, lessonType, dateTime, true
        );
        System.out.println(result.getMessage());
        if (result.isInsufficientPoints()) {
            while (true) {
                try {
                    System.out.println("""
        ポイントを購入しますか？
       1. はい
       2. いいえ
           
    番号を入力してください>>> """);
                    switch (Integer.parseInt(EnglishSchool.sc.nextLine())) {
                        case 1 -> {
                            addPoints();
                            return;
                        }
                        case 2 -> {
                            return;
                        }
                        default -> System.out.println("無効な入力！！！");
                    }
                } catch (Exception e) {
                    System.out.println("数字を入力してください！！！");
                }
            }
        }
    }

    public static void viewLessons() {
        System.out.println(Design.LINE);
        for (Lesson l : LessonService.getActiveLessons()) {
            System.out.println(
                    "レッスンID=" + l.getLessonId() +
                            " 生徒ID=" + l.getStudentId() +
                            " 講師ID=" + l.getTeacherId() +
                            " レッスンタイプ=" + l.getLessonType() +
                            " 日時=" + l.getDateTime() + "時"
            );
        }
    }

    public static void cancelLesson() {
        System.out.println(Design.LINE);
        System.out.print("取消するレッスンID: ");
        int lessonId = Integer.parseInt(EnglishSchool.sc.nextLine());

        ActionResult result = LessonService.cancelLesson(lessonId);
        System.out.println(result.getMessage());
    }

    //teacher

    public static void addTeacher() {
        System.out.println(Design.LINE);
        System.out.print("講師名: ");
        String name = EnglishSchool.sc.nextLine();

        TeacherService.addTeacher(name);
        System.out.println("講師を登録しました。");
    }

    public static void viewTeachers() {
        System.out.println(Design.LINE);
        System.out.println("\n------ 講師一覧 ------");
        for (Teacher t : TeacherService.getTeachers()) {
            System.out.println(
                    "ID=" + t.getId() +
                            " 名前=" + t.getName()
            );
        }
    }

    //money

    public static void addPoints() {
        System.out.println(Design.LINE);
        viewStudents();
        System.out.print("生徒ID: ");
        int id = Integer.parseInt(EnglishSchool.sc.nextLine());
        ActionResult validation = StudentService.validateActiveStudent(id);
        if (!validation.isSuccess()) {
            System.out.println(validation.getMessage());
            return;
        }
        System.out.print("追加ポイント（200単位）: ");
        int p = Integer.parseInt(EnglishSchool.sc.nextLine());
        ActionResult result = StudentService.addPoints(id, p);
        System.out.println(result.getMessage());
    }

    public static void viewLessonCost() {
        System.out.println("\n------ 単価 ------");
        System.out.println("レッスン単価=" + LessonCost.getLessonCost() + "ポイント");
        System.out.println("ポイント単価=" + LessonCost.getPointValue() + "円");
    }

    public static void changeLessonCost(){
        System.out.println(Design.LINE);
        viewLessonCost();
        while(true){
            try{
                System.out.println("""
            単価を変える
       1. はい
       2. いいえ
           
    番号を入力してください>>> """);
                switch (Integer.parseInt(EnglishSchool.sc.nextLine())){
                    case 1 -> {
                        LessonCost.changeCost();
                        return;
                    }
                    case 2 -> {
                        return;
                    }
                    default -> System.out.println("無効な入力！！！");
                }
            } catch (Exception e) {
                System.out.println("数字を入力してください！！！");
            }
        }
    }
    public static void viewProfit() {

        System.out.println(Design.LINE);
        ProfitSummary summary = FinanceService.calculateProfit();
        System.out.println(Design.LINE);
        System.out.println("ポイント合計=" + summary.getTotalPointsUsed());
        System.out.println("売上=" + summary.getTotalProfit() + "円");
    }
}
