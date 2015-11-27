package milleniumbug;

import java.util.Objects;

public class Pair<T, U> {

    public final T first;
    public final U second;

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.first);
        hash = 67 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.second, other.second)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + ')';
    }

}
