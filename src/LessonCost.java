public class LessonCost {
    private static int[] costs = new int[]{1000,50};

    public static void changeCost(){
        System.out.println(Design.LINE);
        System.out.print("一ポイントの単価を入力してください>>> ");
        costs[0] = Integer.parseInt(EnglishSchool.sc.nextLine());
        System.out.print("レッスン1コマの単価を入力してください>>> ");
        costs[1] = Integer.parseInt(EnglishSchool.sc.nextLine());
        CsvUtil.saveLessonCost(costs[0], costs[1]);
        System.out.println("単価を変更しました。");
    }

    public static void setCosts(int pointValue, int lessonCost) {
        costs[0] = pointValue;
        costs[1] = lessonCost;
    }

    public static int getPointValue() {
        return costs[0];
    }

    public static int getLessonCost() {
        return costs[1];
    }
}
