public class ProfitSummary {
    private final int lessonCount;
    private final int totalPointsUsed;
    private final int totalProfit;

    public ProfitSummary(int lessonCount, int totalPointsUsed, int totalProfit) {
        this.lessonCount = lessonCount;
        this.totalPointsUsed = totalPointsUsed;
        this.totalProfit = totalProfit;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public int getTotalPointsUsed() {
        return totalPointsUsed;
    }

    public int getTotalProfit() {
        return totalProfit;
    }
}
