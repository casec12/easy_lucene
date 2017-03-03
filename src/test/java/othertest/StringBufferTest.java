package othertest;

/**
 * Created by chenzhaolei on 2017/3/1.
 */
public class StringBufferTest {
    public static void main(String[] args){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Hello!");
        System.out.println("length:"+stringBuffer.length());
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        System.out.println(stringBuffer.toString());
    }
}
