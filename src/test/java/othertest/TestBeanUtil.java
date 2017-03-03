package othertest;

import com.cncell.bean.Book;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * Created by chenzhaolei on 2017/3/1.
 */
public class TestBeanUtil {
    public static void main(String[] args) throws Exception{
        Book book = new Book();
        book.setId("ID001");
        book.setName("HarryMouse");
//        book.setPages(100);
//        book.setDescribs(new String[]{"desc1", "desc2"});
        Map<String,String> mapping = BeanUtils.describe(book);
        for(Map.Entry<String,String> entry : mapping.entrySet()){
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            System.out.println("fieldName:"+fieldName+",fieldValue:"+fieldValue);
        }



    }
}
