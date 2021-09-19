package si.f5.hatosaba.uhcffa.utils;

public class Simple {
    private static final TimeService TIME_SERVICE = new TimeService();

    public static TimeService time() {
        return TIME_SERVICE;
    }
}
