package space.xingzhi.accelaration;

public class Record {
    private float x;
    private float y;
    private float z;
    private long currentTime;

    Record(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.currentTime = System.currentTimeMillis();
    }

    @Override public String toString() {
        return Long.toHexString(this.currentTime) + ", " +
                Integer.toHexString(Float.floatToIntBits(this.x)) + ", " +
                Integer.toHexString(Float.floatToIntBits(this.y)) + ", " +
                Integer.toHexString(Float.floatToIntBits(this.z)) + "\n";
    }
}
