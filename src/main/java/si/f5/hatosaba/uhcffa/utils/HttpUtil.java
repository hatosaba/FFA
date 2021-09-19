package si.f5.hatosaba.uhcffa.utils;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.File;
import java.util.concurrent.TimeUnit;

public abstract class HttpUtil {

    private static HttpRequest setTimeout(HttpRequest httpRequest) {
        return httpRequest
                .connectTimeout(Math.toIntExact(TimeUnit.SECONDS.toMillis(30)))
                .readTimeout(Math.toIntExact(TimeUnit.SECONDS.toMillis(30)));
    }

    public static String requestHttp(String requestUrl) {
        try {
            return setTimeout(HttpRequest.get(requestUrl)).body();
        } catch (HttpRequest.HttpRequestException e) {
            return "";
        }
    }

    public static void downloadFile(String requestUrl, File destination) {
        try {
            setTimeout(HttpRequest.get(requestUrl)).receive(destination);
        } catch (HttpRequest.HttpRequestException e) { }
    }

    public static boolean exists(String url) {
        try {
            HttpRequest request = setTimeout(HttpRequest.head(url));
            return request.code() / 100 == 2;
        } catch (Exception e) {
            return false;
        }
    }

}
