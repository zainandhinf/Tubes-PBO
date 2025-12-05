package util;

import java.util.function.Predicate;

/**
 * Kelas Validator dengan Generic Programming.
 * Memenuhi Kriteria No. 10.
 * @param <T> Tipe data yang akan divalidasi (String, Double, Integer, dll)
 */
public class Validator<T> {
    
    private final T input;

    public Validator(T input) {
        this.input = input;
    }

    /**
     * Memvalidasi input berdasarkan aturan (Predicate) yang diberikan.
     */
    public boolean validate(Predicate<T> rule) {
        return rule.test(input);
    }
}