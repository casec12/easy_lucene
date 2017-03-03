package othertest;

/**
 * Created by chenzhaolei on 2017/2/27.
 */
public class TestClass {
    public static void main(String[] args){
        String[] arr = new String[]{};
        Class type = arr.getClass();
        String canonicalName = type.getCanonicalName();
        String name = type.getName();
        String simpleName = type.getSimpleName();
        System.out.println("canonicalName:"+canonicalName);
        System.out.println("name:"+name);
        System.out.println("simpleName:"+simpleName);
        System.out.println("isArray:"+type.isArray());
    }
}
