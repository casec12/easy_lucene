package othertest;

import com.cncell.bean.Book;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import with_prototype.easy_lucene.bean.BaseBean;
import with_prototype.easy_lucene.bean.DocBook;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by chenzhaolei on 2017/2/27.
 */
public class BeanUtilTest3 {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DocBook docBook = new DocBook();
        docBook.setId("1");
        docBook.setName("FirstBook");
        docBook.setDocType("Book");
        docBook.setDocId("00001");
        docBook.setDescribs(new String[]{"desc1","desc2"});

        Book book = new Book();

        try {
            BeanUtils.copyProperties(book,docBook);
            System.out.println(book.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        BaseBean documentBean = docBook;
        String name = BeanUtils.getProperty(docBook,"name");
        System.out.println("name:"+name);

        Map<String,String> describeMap = BeanUtils.describe(docBook);
        for(Map.Entry<String,String> entry : describeMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            if("class".equals(key)){
                continue;
            }
            if("describs".equals(key)){
                Object describe0 = PropertyUtils.getIndexedProperty(docBook,"describs[0]");
                Object describe1 = PropertyUtils.getIndexedProperty(docBook,"describs",1);
                System.out.println("describeArr0:"+describe0);
                System.out.println("describeArr1:"+describe1);
            }else{
                System.out.println("Map data:"+key+","+value);
            }
        }


    }
}
