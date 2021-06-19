package store;

import model.Audit;
import java.util.Map;
import java.util.HashMap;

/**
 *  An implementation of the persistent layer for Audit in lieu of RDBMS
 *
 */
public class AuditStore {

    private static Map<String, Audit> audits;

    static {
        audits = new HashMap<String, Audit>();
    }

    public static void add(Audit audit) {
        audits.put(audit.getKey(), audit);
    }

    /**
     * for testing purpose
     * @return
     */
    public static int count() {
        return audits.size();
    }
}
