package vn.ducquoc.jutil.dsa;

/**
 * Generic holder for value pair. (not restrained as java.util.Map.Entry)
 * 
 * @param <T>
 *            first value
 * @param <U>
 *            second value
 * 
 * @see org.apache.commons.lang3.tuple.Pair
 * @see be.abeel.util.Pair
 */
public class Pair<T, U> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private T first;
    private U second;

    /**
     * Default constructor
     */
    public Pair() {
    }

    /**
     * Full constructor, initialize both values.
     * 
     * @param first
     *            the first value
     * @param second
     *            the second value
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Factory method (analogical to C++ standard library's make_pair function);
     * handier than the constructor as it is able to deduce type parameters.
     * 
     * @param first
     *            the first value
     * @param second
     *            the second value
     * @param <T>
     *            first value
     * @param <U>
     *            second value
     * @return created instance
     */
    public static <T, U> Pair<T, U> makePair(T first, U second) {
        return new Pair<T, U>(first, second);
    }

    /**
     * @return the first
     */
    public T getFirst() {
        return first;
    }

    /**
     * @param first
     *            the first to set
     */
    public void setFirst(T first) {
        this.first = first;
    }

    /**
     * @return the second
     */
    public U getSecond() {
        return second;
    }

    /**
     * @param second
     *            the second to set
     */
    public void setSecond(U second) {
        this.second = second;
    }

    //
    // override
    //
    public String toString() {
        return "{first=" + first + ", second=" + second + '}';
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        final Pair<T, U> other = (Pair<T, U>) obj;

        if (first == null) {
            if (other.first != null)
                return false;
        } else if (!first.equals(other.first))
            return false;

        if (second == null) {
            if (other.second != null)
                return false;
        } else if (!second.equals(other.second))
            return false;

        return true;
    }

    public int hashCode() {
        final int prime = 31; // alternative: <37, 7>
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

}
