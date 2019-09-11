package site.jarret.skarner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import site.jarret.skarner.util.ThreadPoolUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 获取文本
 *
 * @author Jarret
 * @since 2019/09/11 14:14
 */
public class CrawlText {
    /***
     * 获取文本
     *
     * @param autoDownloadFile 自动下载文件
     * @param Multithreading 多线程 默认false
     * @param url 网站链接
     * @throws IOException
     */
    public static void getText(boolean autoDownloadFile, boolean Multithreading, String url) throws IOException {
        String rule = "abs:href";
        List<String> urlList = new ArrayList<>();

        Connection connect = Jsoup.connect(url);
        Document document = connect
                .timeout(4000)
                .ignoreContentType(true)
                .userAgent("Mozilla\" to \"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0)")
                .get();

        Elements urlNode = document.select("a[href~=^/2/2057/]");

        for (Element element : urlNode) {
            urlList.add(element.attr(rule));
        }

        long start = System.currentTimeMillis();
        List<Future> futureList = new ArrayList();
        try {
            Future<String> messageFuture = ThreadPoolUtil.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(300);
                    CrawTextThread.run(urlList);
                    return "OK";
                }
            });
            futureList.add(messageFuture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Future<String> message : futureList) {
            try {
                String messageData = message.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(String.format("共计耗时{%s}毫秒", System.currentTimeMillis() - start));
    }
}
