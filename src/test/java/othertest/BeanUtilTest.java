package othertest;

import com.cncell.bean.Book;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by chenzhaolei on 2017/3/2.
 */
public class BeanUtilTest {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Book book = new Book();
        book.setDescribs(new String[]{"str1","str2"});
        String convertResult = BeanUtils.getProperty(book,"describs");
        System.out.println(convertResult);
        Map<String,String> mapping = BeanUtils.describe(book);
        for(Map.Entry<String,String> KV : mapping.entrySet()){
            String fieldName = KV.getKey();
            String fieldValue = KV.getValue();
            System.out.println("fieldName:"+fieldName+",fieldValue:"+fieldValue);
        }
    }
}
