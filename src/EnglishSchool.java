import java.util.*;

public class EnglishSchool {
    public static List<Student> students = new ArrayList<>();
    public static List<Teacher> teachers = new ArrayList<>();
    public static List<Lesson> lessons = new ArrayList<>();
    public static List<LessonCost> lessonCosts = new ArrayList<>();
    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        CsvUtil.loadStudents(students);
        CsvUtil.loadTeachers(teachers);
        CsvUtil.loadLessons(lessons);
        int[] lessonCost = CsvUtil.loadLessonCost();
        if (lessonCost != null) {
            LessonCost.setCosts(lessonCost[0], lessonCost[1]);
        }
        while (true){
            System.out.println(Design.LINE);
            System.out.print("""
              
                        ***** 英会話スクールシステム *****
                        
                                  Main Menu
                                  
                          1. 生徒
                          2. 講師
                          3. 受付
                          0. 終了してデータをCSVに書く
                          
                        """);
            while (true) {
                System.out.print("番号を選択してください >>> ");
                try{
                    int choice = Integer.parseInt(sc.nextLine());
                    switch (choice){
                        case 1 -> {
                            StudentMenu.showMenu();
                        }
                        case 2 -> {
                            TeacherMenu.showMenu();
                        }
                        case 3 -> {
                            StaffMenu.showMenu();
                        }
                        case 0-> {
                            CsvUtil.saveStudents(students);
                            CsvUtil.saveTeachers(teachers);
                            CsvUtil.saveLessons(lessons);
                            System.out.println("保存が終了しました。");
                            return;
                        }
                        default -> {
                            System.out.println("無効な入力！！！");
                            continue;
                        }
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("数字を入力してください");
                }
            }
        }
    }
}
