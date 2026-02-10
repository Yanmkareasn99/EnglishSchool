public class ReserveLessonResult {
    private final boolean success;
    private final boolean insufficientPoints;
    private final String message;

    private ReserveLessonResult(boolean success, boolean insufficientPoints, String message) {
        this.success = success;
        this.insufficientPoints = insufficientPoints;
        this.message = message;
    }

    public static ReserveLessonResult success(String message) {
        return new ReserveLessonResult(true, false, message);
    }

    public static ReserveLessonResult failure(String message) {
        return new ReserveLessonResult(false, false, message);
    }

    public static ReserveLessonResult insufficientPoints(String message) {
        return new ReserveLessonResult(false, true, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isInsufficientPoints() {
        return insufficientPoints;
    }

    public String getMessage() {
        return message;
    }
}
