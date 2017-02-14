package tuple;

/**
 * Created by root on 1/24/17.
 */
public class Tuple<TValue1, TValue2> {

    public final TValue1 value1;
    public final TValue2 value2;

    public Tuple(TValue1 value1, TValue2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (!value1.equals(tuple.value1)) return false;
        return value2.equals(tuple.value2);
    }

    @Override
    public int hashCode() {
        int result = value1.hashCode();
        result = 31 * result + value2.hashCode();
        return result;
    }
}
