package security;

public enum Ops {
    CREATE(0),
    RETRIEVE(1),
    UPDATE(2),
    DELETE(3);

    private int numVal;

    Ops(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
