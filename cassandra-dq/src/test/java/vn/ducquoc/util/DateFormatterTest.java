package vn.ducquoc.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DateFormatterTest {

  @org.junit.Test
  public void testParsingWithCallable() throws Exception {

    // FIXME - dq: To see the bug, use SimpleDateFormat statement
    //final java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
    final DateFormatter df = new vn.ducquoc.util.DateFormatter("yyyyMMdd");

    Callable<Date> task = new Callable<Date>() {
      public Date call() throws Exception {
        return df.parse("20101010");
      }
    };

    // pool with 5 threads
    ExecutorService exec = Executors.newFixedThreadPool(5);
    List<Future<Date>> results = new ArrayList<Future<Date>>();

    // perform 42 date conversions, may adjust 12->120 depending on your computer
    for (int i = 0; i < 42; i++) {
      results.add(exec.submit(task));
    }
    exec.shutdown();

    org.junit.Assert.assertNotNull("Look at the results");
    for (Future<Date> result : results) {
      System.out.println(result.get());
    }
  }

  @org.junit.Test
  public void testFormattingWithRunnable() {

    // FIXME - dq: To see the bug, use SimpleDateFormat statement
    //final java.text.DateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
    final DateFormatter df = new vn.ducquoc.util.DateFormatter("dd-MMM-yyyy");

    final String[] testdata = { "01-Jan-1999", "14-Feb-2001", "31-Dec-2007" };

    Runnable runnables[] = new Runnable[testdata.length];

    org.junit.Assert.assertNotNull("Look at the results");
    for (int i = 0; i < runnables.length; i++) {
      final int i2 = i;
      runnables[i] = new Runnable() {
        public void run() {
          try { // 42 here, may adjust 12->120 depending on your computer
            for (int j = 0; j < 42; j++) {
              String str = testdata[i2];
              String str2 = null;
              /* synchronized(df) */{
                Date d = df.parse(str);
                str2 = df.format(d);
              }
              System.out.println("EXPECTED " + str + " ACTUAL " + str2);
            }
          } catch (java.text.ParseException e) {
            throw new RuntimeException("Parse failed");
          }
        }
      };
      new Thread(runnables[i]).start();
    }
  }

}
