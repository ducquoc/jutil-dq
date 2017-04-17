package vn.ducquoc.jutil.dsa;

/**
 * Generic holder for value Quadra, a.k.a Quartet.
 * 
 * @param <F>
 *          first value
 * @param <S>
 *          second value
 * @param <T>
 *          third value
 * @param <Y>
 *          fourth value
 * 
 * @see org.javatuples.Quartet
 */
public class Quadra<F, S, T, Y> implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  private F first;
  private S second;
  private T third;
  private Y fourth;

  /**
   * Default constructor
   */
  public Quadra() {
  }

  /**
   * Full constructor, initialize both values.
   * 
   * @param first
   *          the first value
   * @param second
   *          the second value
   */
  public Quadra(F first, S second, T third, Y fourth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
  }

  public static <F, S, T, Y> Quadra<F, S, T, Y> makeQuadra(F f, S s, T t,
      Y fourth) {
    return new Quadra<F, S, T, Y>(f, s, t, fourth);
  }

  public F getFirst() {
    return first;
  }

  public void setFirst(F first) {
    this.first = first;
  }

  public S getSecond() {
    return second;
  }

  public void setSecond(S second) {
    this.second = second;
  }

  public T getThird() {
    return third;
  }

  public void setThird(T third) {
    this.third = third;
  }

  public Y getFourth() {
    return fourth;
  }

  public void setFourth(Y fourth) {
    this.fourth = fourth;
  }

  //
  // override
  //
  public String toString() {
    return "{first=" + first + ", second=" + second + ", third=" + third
        + ", fourth=" + fourth + '}';
  }

  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;

    final Quadra<F, S, T, Y> other = (Quadra<F, S, T, Y>) obj;

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

    if (third == null) {
      if (other.third != null)
        return false;
    } else if (!third.equals(other.third))
      return false;

    if (fourth == null) {
      if (other.fourth != null)
        return false;
    } else if (!fourth.equals(other.fourth))
      return false;

    return true;
  }

  public int hashCode() {
    final int prime = 31; // alternative: <37, 7>
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    result = prime * result + ((third == null) ? 0 : third.hashCode());
    result = prime * result + ((fourth == null) ? 0 : fourth.hashCode());
    return result;
  }

}
