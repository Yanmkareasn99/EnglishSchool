//Staff have all the authorities except recordAttandance();
import java.time.LocalDate;
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
        int id = EnglishSchool.students.size()+1;

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

        Student s = new Student(
                id, name, age, sex,
                phone, email, course,
                0, "在籍", "", ""
        );
        s.setRegisterDate(LocalDate.now());

        EnglishSchool.students.add(s);
        System.out.println("生徒を登録しました。");
    }

    public static void viewStudents() {
        System.out.println(Design.LINE);
        System.out.println("\n------ 生徒一覧 ------");
        for (Student s : EnglishSchool.students) {
            if (!"在籍".equals(s.getStatus())) {
                continue;
            }
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

        for (Student s : EnglishSchool.students) {
            if (s.getId() == id) {
                if (!"在籍".equals(s.getStatus())) {
                    System.out.println("在籍中の生徒が見つかりません。");
                    return;
                }
                System.out.print("新しいコース名: ");
                String course = EnglishSchool.sc.nextLine();
                System.out.println("****コース変更****");
                System.out.println("変更前: " + s.getCourse());
                s.setCourse(course);
                System.out.println("変更後: " + course);
                return;
            }
        }
        System.out.println("生徒が見つかりません。");
    }

    public static void removeStudent() {
        System.out.println(Design.LINE);
        System.out.print("退会する生徒ID: ");
        int id = Integer.parseInt(EnglishSchool.sc.nextLine());

        for (Student s : EnglishSchool.students) {
            if (s.getId() == id) {
                if (!"在籍".equals(s.getStatus())) {
                    System.out.println("すでに退学済みです。");
                    return;
                }
                s.setStatus("退学");
                System.out.println("退学処理が完了しました。");

                return;
            }
        }
        System.out.println("生徒が見つかりません。");
    }

    public static Student findStudent(int studentId) {
        for (Student s : EnglishSchool.students) {
            if (s.getId() == studentId) {
                return s;
            }
        }
        return null;
    }



    //lesson

    public static void reserveLesson() {
        System.out.println(Design.LINE);
        viewStudents();
        int lessonId = EnglishSchool.lessons.size()+1;
        System.out.print("生徒ID: ");
        int studentId = Integer.parseInt(EnglishSchool.sc.nextLine());
        Student student = findStudent(studentId);
        if (student == null || !"在籍".equals(student.getStatus())) {
            System.out.println("在籍中の生徒が見つかりません。");
            return;
        }
        viewTeachers();

        System.out.print("講師ID: ");
        int teacherId = Integer.parseInt(EnglishSchool.sc.nextLine());
        Teacher currentTeacher = Student.findTeacher(teacherId);
        if (currentTeacher == null) {
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

        if (!student.consumePoints(LessonCost.getLessonCost())) {
            System.out.println("ポイントが不足しています。");
            while(true){
                try{
                    System.out.println("""
        ポイントを購入しますか？
       1. はい
       2. いいえ
           
    番号を入力してください>>> """);
                    switch (Integer.parseInt(EnglishSchool.sc.nextLine())){
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

        Lesson l = new Lesson(lessonId, studentId, teacherId, lessonType, DateTimeUtil.format(dateTime));
        EnglishSchool.lessons.add(l);

        System.out.println("レッスンを予約しました。");
    }

    public static void viewLessons() {
        System.out.println(Design.LINE);
        for (Lesson l : EnglishSchool.lessons) {
            if (!"取消".equals(l.getStatus())) {
                System.out.println(
                        "レッスンID=" + l.getLessonId() +
                                " 生徒ID=" + l.getStudentId() +
                                " 講師ID=" + l.getTeacherId() +
                                " レッスンタイプ=" + l.getLessonType() +
                                " 日時=" + l.getDateTime() + "時"
                );
            }
        }
    }

    public static void cancelLesson() {
        System.out.println(Design.LINE);
        System.out.print("取消するレッスンID: ");
        int lessonId = Integer.parseInt(EnglishSchool.sc.nextLine());

        for (Lesson l : EnglishSchool.lessons) {
            if (l.getLessonId() == lessonId) {
                if ("取消".equals(l.getStatus())) {
                    System.out.println("すでに取消済みです。");
                    return;
                }
                l.setStatus("取消");
                System.out.println("レッスンを取消しました。");
                return;
            }
        }
        System.out.println("レッスンが見つかりません。");
    }

    //teacher

    public static void addTeacher() {
        System.out.println(Design.LINE);
        int id = EnglishSchool.teachers.size() + 1;

        System.out.print("講師名: ");
        String name = EnglishSchool.sc.nextLine();

        Teacher t = new Teacher(id, name);
        EnglishSchool.teachers.add(t);
        System.out.println("講師を登録しました。");
    }

    public static void viewTeachers() {
        System.out.println(Design.LINE);
        System.out.println("\n------ 講師一覧 ------");
        for (Teacher t : EnglishSchool.teachers) {
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

        for (Student s : EnglishSchool.students) {
            if (s.getId() == id) {
                if (!"在籍".equals(s.getStatus())) {
                    System.out.println("在籍中の生徒が見つかりません。");
                    return;
                }
                System.out.print("追加ポイント（200単位）: ");
                int p = Integer.parseInt(EnglishSchool.sc.nextLine());

                if (s.addPoints(p)) {
                    System.out.println("ポイント追加完了");
                } else {
                    System.out.println("200単位で入力してください");
                }
                return;
            }
        }
        System.out.println("生徒が見つかりません。");
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
        int lessonCount = 0;
//        for (Lesson l : EnglishSchool.lessons) {
//            if (!"取消".equals(l.getStatus())) {
//                lessonCount++;
//            }
//        }
        for (Lesson l : EnglishSchool.lessons) {
                lessonCount++;
        }
        int totalPointsUsed = lessonCount * LessonCost.getLessonCost();
        int totalProfit = totalPointsUsed * LessonCost.getPointValue();
        System.out.println(Design.LINE);
        System.out.println("ポイント合計=" + totalPointsUsed);
        System.out.println("売上=" + totalProfit + "円");
    }
}
