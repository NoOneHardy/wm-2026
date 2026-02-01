package ch.no1hardy.service.shared;

public class RequiredValidator {
    public boolean isBlank(String str) {
        return isNull(str) || str.isBlank();
    }

    private boolean isNull(Object obj) {
        return obj == null;
    }
}
