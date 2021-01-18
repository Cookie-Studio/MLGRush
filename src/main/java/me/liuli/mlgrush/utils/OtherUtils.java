package me.liuli.mlgrush.utils;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OtherUtils {
    public static String y2j(File file){
        Config yamlConfig = new Config(file,Config.YAML);
        ConfigSection section = yamlConfig.getRootSection();
        return new GsonBuilder().create().toJson(section);
    }
    public static void injectClass(File file) {
        try {
            URLClassLoader autoload = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(autoload, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeFile(String path,String text) {
        try {
            Writer writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
            writer.write(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String readFile(String fileName) {
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
            return new String(filecontent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void readJar(String fileName,String JarDir,String path){
        try {
            JarFile jarFile = new JarFile(JarDir);
            JarEntry entry = jarFile.getJarEntry(fileName);
            InputStream input = jarFile.getInputStream(entry);
            java.nio.file.Files.copy(input, new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
            input.close();
            jarFile.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void copyFile(File orifile,File tofile) throws IOException {
        FileInputStream infile=new FileInputStream(orifile);
        FileOutputStream outfile=new FileOutputStream(tofile);
        FileChannel inc=infile.getChannel();
        FileChannel outc=outfile.getChannel();
        inc.transferTo(0,inc.size(),outc);
        inc.close();
        outc.close();
        infile.close();
        outfile.close();
    }
    public static void copyDir(String oridir,String todir) throws IOException {
        new File(todir).mkdirs();
        File[] flist=new File(oridir).listFiles();
        for(int i=0;i<flist.length;i++){
            if(flist[i].isFile()){
                copyFile(flist[i],new File(new File(todir).getPath()+"/"+flist[i].getName()));
            }else{
                String faps=flist[i].getPath();
                new File(new File(todir).getPath()+"/"+faps.substring(new File(oridir).getPath().length())+"/").mkdirs();
                copyDir(faps,new File(todir).getPath()+"/"+faps.substring(new File(oridir).getPath().length())+"/");
            }
        }
    }
    public static void delDir(String dir) throws IOException {
        File[] flist=new File(dir).listFiles();
        for(int i=0;i<flist.length;i++){
            if(flist[i].isFile()){
                flist[i].delete();
            }else{
                delDir(flist[i].getPath());
            }
        }
        new File(dir).delete();
    }
}
