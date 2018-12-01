package models;

public class Key {
    private static String code;
    private static Key instance;

    private Key() {
    }

    public static Key getInstance(String c){
        if(instance == null){
            instance = new Key();
            code = c;
        }
        return instance;
    }

    public static String getCode() {
        return code;
    }

    public static void resetCode(){
        Key.code = null;
    }
}