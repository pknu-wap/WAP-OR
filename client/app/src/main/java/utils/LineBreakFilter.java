package utils;
import android.text.InputFilter;
import android.text.Spanned;

public class LineBreakFilter implements InputFilter {
    private final int maxLines;

    public LineBreakFilter(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 기존 텍스트와 새로운 텍스트 결합
        String newText = new StringBuilder(dest).replace(dstart, dend, source.subSequence(start, end).toString()).toString();

        // 줄바꿈 개수 확인
        int lineCount = newText.split("\n").length - 1;
        if (lineCount >= maxLines) {
            return ""; // 줄바꿈 제한 초과 시 입력 차단
        }
        return null; // 정상 입력
    }
}