package ch.no1hardy.service.shared;

public abstract class BaseValidator<T> {
    protected abstract void validate(T object);

    protected boolean isBlank(String str) {
        return isNull(str) || str.isBlank();
    }

    protected boolean isNull(Object obj) {
        return obj == null;
    }
}
