public class FinanceService {
    public static ProfitSummary calculateProfit() {
        int lessonCount = 0;
        for (Lesson l : EnglishSchool.lessons) {
            lessonCount++;
        }
        int totalPointsUsed = lessonCount * LessonCost.getLessonCost();
        int totalProfit = totalPointsUsed * LessonCost.getPointValue();
        return new ProfitSummary(lessonCount, totalPointsUsed, totalProfit);
    }
}
