package dev.kazi.mcservercontroller.commands;

import java.util.List;
import java.util.ArrayList;
import java.beans.ConstructorProperties;

import lombok.NonNull;

public class CommandTokenizer {
    public static Pair<List<String>, List<Integer>> tokenizeCommand(@NonNull String line) {
        if (line == null)
            throw new NullPointerException("line");
        List<String> output = new ArrayList<>();
        List<Integer> outputIndices = new ArrayList<>();
        outputIndices.add(0);
        StringBuilder stringBuilder = new StringBuilder();
        boolean escaped = false;
        boolean insideQuote = false;
        int idx = 0;
        for (char c : line.toCharArray()) {
            idx++;
            if (escaped) {
                stringBuilder.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                if (insideQuote) {
                    insideQuote = false;
                    if (stringBuilder.length() > 0) {
                        output.add(stringBuilder.toString());
                        outputIndices.add(idx);
                        stringBuilder.setLength(0);
                    }
                } else {
                    insideQuote = true;
                }
            } else if (c == ' ' && !insideQuote) {
                if (stringBuilder.length() > 0) {
                    output.add(stringBuilder.toString());
                    outputIndices.add(idx);
                    stringBuilder.setLength(0);
                }
            } else {
                stringBuilder.append(c);
            }
        }
        if (stringBuilder.length() > 0)
            output.add(stringBuilder.toString());
        return new Pair<>(output, outputIndices);
    }

    public static final class Pair<T, U> {

        private final T first;
        private final U second;

        @ConstructorProperties({"first", "second"})
        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Pair)) return false;
            Pair<?, ?> other = (Pair<?, ?>) o;
            Object thisFirst = getFirst(), otherFirst = other.getFirst();
            if ((thisFirst == null) ? (otherFirst != null) : !thisFirst.equals(otherFirst))
                return false;
            Object thisSecond = getSecond(), otherSecond = other.getSecond();
            return !((thisSecond == null) ? (otherSecond != null) : !thisSecond.equals(otherSecond));
        }

        public String toString() {
            return "CommandTokenizer.Pair(first=" + getFirst() + ", second=" + getSecond() + ")";
        }

        public T getFirst() {
            return this.first;
        }

        public U getSecond() {
            return this.second;
        }
    }
}