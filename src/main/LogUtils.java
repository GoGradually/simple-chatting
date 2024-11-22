package main;

public class LogUtils {
    public static void log(String message){
        System.out.println(Thread.currentThread().getName() + ": " + message);
    }
}
