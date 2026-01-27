//Staff have all the authorities except recordAttandance();
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class StaffMenu {

    public static void showMenu() {
        while (true) {
            try {
                System.out.println(Main.LINE);
                System.out.print("""
                       
                        1: 生徒登録
                        2: 生徒一覧
                        3: 生徒情報変更
                        4: ポイント購入
                        5: レッスン予約
                        6: レッスン確認
                        7: レッスンキャンセル
                        8: 講師登録
                        9: 講師一覧
                        10: 生徒退会
                        0: 戻る
                       
                       番号を入力してください>>> """);


                int choice = Integer.parseInt(Main.sc.nextLine());

                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewStudents();
                    case 3 -> changeStudent();
                    case 4 -> addPoints();
                    case 5 -> reserveLesson();
                    case 6 -> viewLessons();
                    case 7 -> cancelLesson();
                    case 8 -> addTeacher();
                    case 9 -> viewTeachers();
                    case 10 -> removeStudent();
                    case 0 -> { return; }
                    default -> System.out.println("無効な入力です。");
                }

            } catch (Exception e) {
                System.out.println("数字を入力してください。");
            }
        }
    }

    public static void addStudent() {
        System.out.println(Main.LINE);
        int id = Main.students.size()+1;

        System.out.print("名前: ");
        String name = Main.sc.nextLine();

        System.out.print("年齢: ");
        int age = Integer.parseInt(Main.sc.nextLine());

        System.out.print("性別: ");
        String sex = Main.sc.nextLine();

        System.out.print("電話番号: ");
        int phone = Integer.parseInt(Main.sc.nextLine());

        System.out.print("Email: ");
        String email = Main.sc.nextLine();

        String course = CourseUtil.selectCourse();

        Student s = new Student(
                id, name, age, sex,
                phone, email, course,
                0, "在籍", "", ""
        );
        s.setRegisterDate(java.time.LocalDate.now());

        Main.students.add(s);
        System.out.println("生徒を登録しました。");
    }

    public static void viewStudents() {
        System.out.println(Main.LINE);
        System.out.println("\n------ 生徒一覧 ------");
        for (Student s : Main.students) {
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
        System.out.println(Main.LINE);
        System.out.print("変更する生徒ID: ");
        int id = Integer.parseInt(Main.sc.nextLine());

        for (Student s : Main.students) {
            if (s.getId() == id) {
                if (!"在籍".equals(s.getStatus())) {
                    System.out.println("在籍中の生徒が見つかりません。");
                    return;
                }
                System.out.print("新しいコース名: ");
                String course = Main.sc.nextLine();
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
        System.out.println(Main.LINE);
        System.out.print("退会する生徒ID: ");
        int id = Integer.parseInt(Main.sc.nextLine());

        for (Student s : Main.students) {
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
        for (Student s : Main.students) {
            if (s.getId() == studentId) {
                return s;
            }
        }
        return null;
    }

    public static void addPoints() {
        System.out.println(Main.LINE);
        System.out.print("生徒ID: ");
        int id = Integer.parseInt(Main.sc.nextLine());

        for (Student s : Main.students) {
            if (s.getId() == id) {
                if (!"在籍".equals(s.getStatus())) {
                    System.out.println("在籍中の生徒が見つかりません。");
                    return;
                }
                System.out.print("追加ポイント（200単位）: ");
                int p = Integer.parseInt(Main.sc.nextLine());

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

    public static void reserveLesson() {
        System.out.println(Main.LINE);
        int lessonId = Main.lessons.size()+1;

        System.out.print("生徒ID: ");
        int studentId = Integer.parseInt(Main.sc.nextLine());
        Student student = findStudent(studentId);
        if (student == null || !"在籍".equals(student.getStatus())) {
            System.out.println("在籍中の生徒が見つかりません。");
            return;
        }

        System.out.print("講師ID: ");
        int teacherId = Integer.parseInt(Main.sc.nextLine());

        String lessonType = CourseUtil.SelectLessonType();

        if (!student.consumePoints(50)) {
            System.out.println("ポイントが不足しています（必要: 50）。");
            return;
        }

        System.out.print("日時 (例: 2026-02-01 18:00): ");
        String input = Main.sc.nextLine();
        LocalDateTime dateTime;
        try {
            dateTime = DateTimeUtil.parse(input);
        } catch (DateTimeParseException e) {
            System.out.println("日時の形式が正しくありません。");
            return;
        }
        if (dateTime.isBefore(LocalDateTime.now())) {
            System.out.println("過去の日時は予約できません。");
            return;
        }

        Lesson l = new Lesson(lessonId, studentId, teacherId, lessonType, DateTimeUtil.format(dateTime));
        Main.lessons.add(l);

        System.out.println("レッスンを予約しました。");
    }

    public static void viewLessons() {
        System.out.println(Main.LINE);
        for (Lesson l : Main.lessons) {
            if (!"取消".equals(l.getStatus())) {
                System.out.println(
                        "レッスンID=" + l.getLessonId() +
                                " 生徒ID=" + l.getStudentId() +
                                " コース=" + l.getCourse() +
                                " 日時=" + l.getDateTime()
                );
            }
        }
    }

    public static void cancelLesson() {
        System.out.println(Main.LINE);
        System.out.print("取消するレッスンID: ");
        int lessonId = Integer.parseInt(Main.sc.nextLine());

        for (Lesson l : Main.lessons) {
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

    public static void addTeacher() {
        System.out.println(Main.LINE);
        int id = Main.teachers.size() + 1;

        System.out.print("講師名: ");
        String name = Main.sc.nextLine();

        Teacher t = new Teacher(id, name);
        Main.teachers.add(t);
        System.out.println("講師を登録しました。");
    }

    public static void viewTeachers() {
        System.out.println(Main.LINE);
        System.out.println("\n------ 講師一覧 ------");
        for (Teacher t : Main.teachers) {
            System.out.println(
                    "ID=" + t.getId() +
                            " 名前=" + t.getName()
            );
        }
    }




}
