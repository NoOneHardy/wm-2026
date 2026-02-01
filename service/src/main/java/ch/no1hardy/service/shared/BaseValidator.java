package ch.no1hardy.service.shared;

public interface BaseValidator<T> {
    void validate(T object);
}
