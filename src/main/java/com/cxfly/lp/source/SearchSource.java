package com.cxfly.lp.source;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchSource {

    public static void main(String[] args) throws Exception {
        SearchSource t = new SearchSource();
        //        System.out.println(t.match("242wrfew旋242fsdfs", "旋"));
        //        byte[] buf = new byte[1024 * 1000];
        //        String string = new String(buf, "GBK");
        //        System.out.println("content: "  );
        t.search("F:\\M\\com\\alibaba");

        //        String appName  = System.getProperty("project.name");
        //        System.out.println(appName);
    }

    private void search(String dirStr) throws Exception {
        File dir = new File(dirStr);
        File[] listFiles = dir.listFiles();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                //                System.out.println("----dir:" + file.getPath());
                this.search(file.getPath());
            } else {
                if (file.getName().toLowerCase().endsWith("sources.jar")) {
                    //System.out.println("file=" + file.getName());
                    JarInputStream jIns = new JarInputStream(new FileInputStream(file));
                    for (JarEntry jarEntry = null; (jarEntry = jIns.getNextJarEntry()) != null;) {
                        String entryName = jarEntry.getName();
                        if (!jarEntry.isDirectory() && entryName.endsWith(".java")) {
                            // System.out.println(entryName);

                            byte[] buf = new byte[1024 * 100];
                            while (jIns.read(buf) != -1) {
                                String content = new String(buf, "GBK");
                                content = content.trim();
                                //System.out.println(content);
                                boolean match = this.match(content, "setLayoutEnabled");
                                if (match) {
                                    System.out.println("------>>>matchReslt : " + entryName);
                                    // System.out.println(content);
                                }
                                buf = new byte[1024 * 100];
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean match(String source, String parten) {
        Pattern pattern = Pattern.compile(parten);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            System.out.println(matcher.group());
            return true;
        }

        return false;
    }
}
