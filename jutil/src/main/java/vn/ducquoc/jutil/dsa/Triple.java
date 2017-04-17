package vn.ducquoc.jutil.dsa;

/**
 * Generic holder for value Triple, a.k.a Triplet.
 * 
 * @param <L>
 *          left value
 * @param <M>
 *          middle value
 * @param <R>
 *          right value
 * 
 * @see org.apache.commons.lang3.tuple.Triple
 * @see com.facebook.collections.Triple
 * @see org.javatuples.Triplet
 */
public class Triple<L, M, R> implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  private L left;
  private M middle;
  private R right;

  /**
   * Default constructor
   */
  public Triple() {
  }

  /**
   * Full constructor, initialize all values.
   */
  public Triple(L left, M middle, R right) {
    this.left = left;
    this.middle = middle;
    this.right = right;
  }

  public static <L, M, R> Triple<L, M, R> makeTriple(L left, M middle, R right) {
    return new Triple<L, M, R>(left, middle, right);
  }

  public L getLeft() {
    return left;
  }

  public void setLeft(L left) {
    this.left = left;
  }

  public M getMiddle() {
    return middle;
  }

  public void setMiddle(M middle) {
    this.middle = middle;
  }

  public R getRight() {
    return right;
  }

  public void setRight(R right) {
    this.right = right;
  }

  //
  // override
  //
  public String toString() {
    return "{left=" + left + ", middle=" + middle + ", right=" + right + '}';
  }

  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;

    final Triple<L, M, R> other = (Triple<L, M, R>) obj;

    if (left == null) {
      if (other.left != null)
        return false;
    } else if (!left.equals(other.left))
      return false;

    if (middle == null) {
      if (other.middle != null)
        return false;
    } else if (!middle.equals(other.middle))
      return false;

    if (right == null) {
      if (other.right != null)
        return false;
    } else if (!right.equals(other.right))
      return false;

    return true;
  }

  public int hashCode() {
    final int prime = 31; // alternative: <37, 7>
    int result = 1;
    result = prime * result + ((left == null) ? 0 : left.hashCode());
    result = prime * result + ((middle == null) ? 0 : middle.hashCode());
    result = prime * result + ((right == null) ? 0 : right.hashCode());
    return result;
  }

}
