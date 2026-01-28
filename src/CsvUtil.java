import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CsvUtil {

    public static void saveStudents(List<Student> students) {
        try (FileWriter fw = new FileWriter("students.csv")) {

            fw.write("id,name,age,sex,phone,email,point,course,lesson,status,address,pointExpireAt,registerDate\n");

            for (Student s : students) {
                fw.write(s.toCsv() + "\n");
            }

        } catch (Exception e) {
            System.out.println("生徒のCSV保存エラー");
        }
    }

    public static void saveTeachers(List<Teacher> teachers) {
        try (FileWriter fw = new FileWriter("teachers.csv")) {

            fw.write("id,name\n");

            for (Teacher t : teachers) {
                fw.write(t.toCsv() + "\n");
            }

        } catch (Exception e) {
            System.out.println("講師CSV保存エラー");
        }
    }

    public static void saveLessons(List<Lesson> lessons) {
        try (FileWriter fw = new FileWriter("lessons.csv")) {

            fw.write("lessonId,studentId,teacherId,course,dateTime,status\n");

            for (Lesson l : lessons) {
                fw.write(l.toCsv() + "\n");
            }

        } catch (Exception e) {
            System.out.println("レッスンCSV保存エラー");
        }
    }

    public static void saveLessonCost(int pointValue, int lessonCost) {
        try (FileWriter fw = new FileWriter("lesson_cost.csv")) {
            fw.write("pointValue,lessonCost\n");
            fw.write(pointValue + "," + lessonCost + "\n");
        } catch (Exception e) {
            System.out.println("単価CSV保存エラー");
        }
    }

    public static int[] loadLessonCost() {
        try (FileReader fr = new FileReader("lesson_cost.csv");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();
            line = br.readLine();
            if (line == null || line.isEmpty()) {
                return null;
            }
            String[] d = line.split(",");
            if (d.length < 2) {
                return null;
            }
            return new int[]{Integer.parseInt(d[0]), Integer.parseInt(d[1])};
        } catch (Exception e) {
            return null;
        }
    }

//    public static void saveProfits(List<Profit> profits) {
//        try (FileWriter fw = new FileWriter("profits.csv")) {
//
//            fw.write("lessonId,studentId,dateTime,status\n");
//
//            for (Profit p : profits) {
//                fw.write(p.toCsv() + "\n");
//            }
//
//        } catch (Exception e) {
//            System.out.println("売上CSV保存エラー");
//        }
//    }

    public static void loadStudents(List<Student> students) {
        try (FileReader fr = new FileReader("students.csv");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");

                LocalDateTime pointExpireAt = null;
                if (d.length > 11 && !d[11].isEmpty()) {
                    pointExpireAt = DateTimeUtil.parse(d[11]);
                }
                LocalDate registerDate = null;
                if (d.length > 12 && !d[12].isEmpty()) {
                    registerDate = LocalDate.parse(d[12]);
                }

                Student s = new Student(
                        Integer.parseInt(d[0]),         // id
                        d[1],                           // name
                        Integer.parseInt(d[2]),         // age
                        d[3],                           // sex
                        Long.parseLong(d[4]),         // phone
                        d[5],                           // email
                        d[7],                           // course
                        Integer.parseInt(d[6]),         // point
                        d[9],                           // status
                        d[8],                           // lesson
                        d.length > 10 ? d[10] : "",     // address
                        pointExpireAt,
                        registerDate
                );

                students.add(s);
            }

        } catch (Exception e) {
            System.out.println("CSVがないです。");
        }
    }

    public static void loadTeachers(List<Teacher> teachers) {
        try (FileReader fr = new FileReader("teachers.csv");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");

                Teacher t = new Teacher(
                        Integer.parseInt(d[0]), // id
                        d[1]                     // name
                );

                teachers.add(t);
            }

        } catch (Exception e) {
            System.out.println("CSVがないです。");
        }
    }

    public static void loadLessons(List<Lesson> lessons) {
        try (FileReader fr = new FileReader("lessons.csv");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");

                String status = d.length > 5 ? d[5] : "予約";

                Lesson l = new Lesson(
                        Integer.parseInt(d[0]), // lessonId
                        Integer.parseInt(d[1]), // studentId
                        Integer.parseInt(d[2]), // teacherId
                        d[3],                   // course
                        d[4],                   // dateTime
                        status
                );

                lessons.add(l);
            }

        } catch (Exception e) {
            System.out.println("CSVがないです。");
        }
    }
}
