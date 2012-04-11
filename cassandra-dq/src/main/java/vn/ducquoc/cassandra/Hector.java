package vn.ducquoc.cassandra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;

public class Hector {

  /**
   * Checks if the given keyspace exists.
   * 
   * @param cluster
   * @param keyspaceName
   * @return boolean
   */
  public static boolean keyspaceExists(Cluster cluster, String keyspaceName) {
    return cluster.describeKeyspace(keyspaceName) != null;
  }

  /**
   * Checks if the given column family exists.
   * 
   * @param cluster
   * @param keyspaceName
   * @param cfName
   * @return boolean
   */
  public static boolean columnFamilyExists(Cluster cluster, String keyspaceName, String cfName) {
    KeyspaceDefinition ksDef = cluster.describeKeyspace(keyspaceName);

    if (ksDef != null) {
      for (ColumnFamilyDefinition cfDef : ksDef.getCfDefs()) {
        if (cfDef.getName().equals(cfName)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Retrieves a map from a list of HColumns.
   * 
   * @param hColumns
   * @return map
   * @see {@link http://wiki.apache.org/cassandra/DataModel }
   */
  public static Map<String, String> hColumnsToMap(List<HColumn<String, String>> hColumns) {
    if (hColumns == null) {
      return null;
    }
    Map<String, String> resultMap = new HashMap<String, String>();
    for (HColumn<String, String> column : hColumns) {
      resultMap.put(column.getName(), column.getValue());
    }
    return resultMap;
  }

  public static void createKeyspace(Cluster cluster, String keyspaceName) {
    KeyspaceDefinition definition = new ThriftKsDef(keyspaceName);
    cluster.addKeyspace(definition, true);
  }

}
