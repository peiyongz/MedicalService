package security;

public class Permission {

    public static Permission PERMISSION_ALL;
    public static Permission PERMISSION_NONE;
    public static Permission PERMISSION_RETRIEVE_ONLY;

    private static final String PERM_ALL = "1111";
    private static final String PERM_RETRIEVE_ONLY = "0100";
    private static final String PERM_NONE   = "0000";

    private String permStr;

    static {
        PERMISSION_ALL           = new Permission(PERM_ALL);
        PERMISSION_NONE          = new Permission(PERM_NONE);
        PERMISSION_RETRIEVE_ONLY = new Permission(PERM_RETRIEVE_ONLY);
    }

    public Permission(String permStr) {
        this.permStr = permStr;
        //CREATE(0), RETRIEVE(1), UPDATE(2), DELETE(3);
        //assert permStr.length == 4
    }

    public boolean authorize(Ops ops) {
        return permStr.charAt(ops.getNumVal()) == '1';
    }
}
