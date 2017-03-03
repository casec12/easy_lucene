package othertest;

/**
 * Created by chenzhaolei on 2017/3/1.
 */
public class TestInitStringBuffer {
    public static void main(String[] args){
        StringBuffer stringBuffer = new StringBuffer();
        if("".equals(stringBuffer)){
            System.out.println("1");
        }
        if("".equals(stringBuffer.toString())){
            System.out.println("2");
        }
    }
}
