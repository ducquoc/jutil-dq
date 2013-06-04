package vn.ducquoc.visual.heatmap;

/**
 * Simple tuple - 2 objects.
 * 
 * @author ducquoc
 * 
 * @param <T1>
 * @param <T2>
 */
@SuppressWarnings({ "rawtypes" })
public class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public int hashCode() {
        return first.hashCode() + 13 * second.hashCode();
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 instanceof Pair) {
            Pair op2 = (Pair) o2;
            return this.first.equals(op2.first) && this.second.equals(op2.second);
        }
        return false;
    }

}
