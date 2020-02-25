import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    //regex:  href="(http:\/\/.*?\.html)" title="(.*?)"
    //regex2: href="(http:\/\/.*?\.jpg)"

    public static void main(String[] args) {
        File f = new File("C:/Users/User/Desktop/ParsedPages");
        if (f.mkdir()) {
            System.out.println("Directory Created");
        } else {
            System.out.println("Directory is not created");
        }

        try {
            int countPages = 1;
            int goalNumberOfGirls = 5;
            int currentCountOfGirls = 0;

            while (currentCountOfGirls < goalNumberOfGirls) {
                String numberOfPage = String.valueOf(countPages);
                URL url = new URL("http://persikoff.com/page/" + numberOfPage);
                URLConnection urlConnection = url.openConnection();
                InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream(), "windows-1251");
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                StringBuilder sb = new StringBuilder();

                while ((str = br.readLine()) != null) {
                    sb.append(str + "\n");
                }

                str = sb.toString();
                br.close();
                isr.close();
                System.out.println(str);

                Pattern pattern = Pattern.compile("href=\"(http:\\/\\/.*?\\.html)\" title=\"(.*?)\"");
                Matcher matcher = pattern.matcher(str);

                while (currentCountOfGirls < goalNumberOfGirls && matcher.find()) {
                    currentCountOfGirls++;
                    System.out.println(currentCountOfGirls);

                    URL url2 = new URL(matcher.group(1));
                    URLConnection urlConnection2 = url2.openConnection();
                    InputStreamReader isr2 = new InputStreamReader(urlConnection2.getInputStream(), "windows-1251");
                    BufferedReader br2 = new BufferedReader(isr2);
                    String str2 = null;
                    StringBuilder sb2 = new StringBuilder();

                    while ((str2 = br2.readLine()) != null) {
                        sb2.append(str2 + "\n");
                    }
                    str2 = sb2.toString();
                    br2.close();
                    isr2.close();
                    Pattern pattern2 = Pattern.compile("href=\"(http:\\/\\/.*?\\.jpg)\"");
                    Matcher matcher2 = pattern2.matcher(str2);
                    System.out.println(matcher.group(1));

                    String nameDir = "C:/Users/User/Desktop/ParsedPages/" + matcher.group(2);
                    System.out.println(nameDir);
                    File file = new File(nameDir);
                    if (file.mkdir()) {
                        System.out.println("Directory Created");
                    } else {
                        System.out.println("Directory is not created");
                    }

                    int count = 0;
                    while (matcher2.find()) {
                        String nameFile = nameDir + "/" + count + ".jpg";
                        if (!new File(nameFile).exists()) {
                            System.out.println(matcher2.group(1));
                            URL url3 = new URL(matcher2.group(1));
                            InputStream in = new BufferedInputStream(url3.openStream());
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buf = new byte[1024];
                            int n = 0;

                            while (-1 != (n = in.read(buf))) {
                                out.write(buf, 0, n);
                            }

                            out.close();
                            in.close();
                            byte[] response = out.toByteArray();
                            count++;

                            FileOutputStream fos = new FileOutputStream(nameFile);
                            fos.write(response);
                            fos.close();
                        }
                    }
                }
                countPages++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

