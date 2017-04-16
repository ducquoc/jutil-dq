package vn.ducquoc.jutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Helper class for comparison which pins certain item at top.
 * 
 * @author ducquoc
 * @see com.google.common.collect.Ordering
 * @see com.google.common.collect.Comparators
 * @see org.springframework.shell.support.util.NaturalOrderComparator
 */
public class PinnedTextComparator<T> implements Comparator<T> {

  List<T> pinnedItems = new ArrayList<T>();
  
  public PinnedTextComparator(T... pinnedItems) {
    if (pinnedItems != null && pinnedItems.length > 0) {
      HashSet<T> set = new HashSet<T>(Arrays.asList(pinnedItems));
      this.pinnedItems.addAll(set);
    }
  }

  public int compare(T o1, T o2) {
    if (this.pinnedItems.contains(o1)) {
      return 1;
    }
    if (this.pinnedItems.contains(o2)) {
      return -1;
    }
    if (o1 != null && o2 != null) {
      return o1.toString().compareTo(o2.toString());
    }
    if (o1 != null) return 1;
    return -1;
  }

}
