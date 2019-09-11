package site.jarret.skarner;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Jarret
 * @since 2019/09/11 14:25
 */
public class CrawTextThread {
    String rule = "";
    String rule_title = "h1";
    String rule_content = "content";

    public static String PATH = "F:\\jsoup\\";

    /**
     * 创建文件
     *
     * @param fileName
     * @return
     */
    public static void createFile(File fileName) throws Exception {
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeTxtFile(String content, File fileName) throws Exception {
        RandomAccessFile mm = null;
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes(StandardCharsets.UTF_8));
            o.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (mm != null) {
                mm.close();
            }
        }
    }

    public static void run(List<String> urlList) {
        for (String url : urlList) {
            try {
                HttpClient client = HttpClients.createDefault();
                HttpGet get = new HttpGet(url);
                get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
                CloseableHttpResponse response = (CloseableHttpResponse) client.execute(get);
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity,"UTF-8");
                Document document = Jsoup.parseBodyFragment(entityStr);
                System.out.println(document.toString());
                String title = document.select("h1").toString();
                System.out.println("title:"+title);
                String content = document.select("#content").html();

                File file = new File(PATH + title.replaceAll("<h1>", "").replaceAll("</h1>", "") + ".txt");
                createFile(file);
                System.out.println("创建文件:" + file.getPath());
                writeTxtFile(fileterHtml(content), file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String fileterHtml(String str) {
        return str.replaceAll(" ", "").replaceAll("<br>", "\r\n");
    }
}
