package with_prototype.easy_lucene.example;

import com.cncell.bean.Book;
import with_prototype.easy_lucene.service.EasyLuceneService;

/**
 * 这版用LuceneTool实现
 * //TODO
 */
public class Example {
/*old code
        public static void main(String[] args) throws Exception {
            Book book1 = new Book();
            book1.setId("id001");
            book1.setName("BookNo1");
            book1.setPages(100);
            book1.setDescribs(new String[]{"desc1", "desc2"});
            DocBook docBook1 = TransferUtils.toDocBean(book1, "BOOK");
            LuceneUtils.updateBeanIndex(docBook1);

            Book book2 = new Book();
            book2.setId("id002");
            book2.setName("BookNo2");
            book2.setPages(200);
            book2.setDescribs(new String[]{"desc2", "desc3"});
            DocBook docBook2 = TransferUtils.toDocBean(book2, "BOOK");
            LuceneUtils.updateBeanIndex(docBook2);

            Book book3 = new Book();
            book3.setId("id003");
            book3.setName("bookno3");
            book3.setPages(300);
            book3.setDescribs(new String[]{"desc3", "desc1", "desc2"});
            DocBook docBook3 = TransferUtils.toDocBean(book3, "book");
            LuceneUtils.updateBeanIndex(docBook3);

            Book searchBook = new Book();
            searchBook.setId("id003");
            searchBook.setName("bookno3");
            searchBook.setPages(300);
            searchBook.setDescribs(new String[]{"desc1", "desc2", "desc3"});
            DocBook searchDocBook = TransferUtils.toDocBean(searchBook, "book");
            System.out.println("显示查询结果：");
            List<DocBook> searchedDocBookList = LuceneUtils.searchBean(searchDocBook);

//           LuceneUtils.deleteBeanIndex(docBook);
//           System.out.println("显示查询结果：");
//           List<DocBook> searchedDocBookList = LuceneUtils.searchBean(docBook);
        }
*/
    public static void main(String[] args) throws Exception {
        Book book1 = new Book();
        book1.setId("id001");
        //TODO 这里有bug，若此字段为大写，会被默认标准分词器处理，实际存小写，导致下面查不到，可通过检索字段统一转小写处理
        book1.setName("bookno1");
        book1.setPages(100);
        book1.setDescribs(new String[]{"desc1", "desc2"});

        Book book2 = new Book();
        book2.setId("id002");
        book2.setName("bookno2");
        book2.setPages(300);
        book2.setDescribs(new String[]{"desc3", "desc1", "desc2"});

        System.out.println("索引book1");//索引book1
        EasyLuceneService.updateBeanIndex(book1,"book");
        System.out.println("索引book2");//索引book2
        EasyLuceneService.updateBeanIndex(book2,"book");

        Book searchedBookBean1 =  new Book();
        searchedBookBean1.setId("id001");
        searchedBookBean1.setName("bookno1");
        //TODO 这里有bug，如不传该字段，因BusBean为int类型，值为0，相当于setPages(0)会查不到。
        searchedBookBean1.setPages(100);
        searchedBookBean1.setDescribs(new String[]{"desc2", "desc1"});
        System.out.println("============查询book1，带id字段");//查询book1，带id字段
        EasyLuceneService.searchBean(searchedBookBean1,"book");

        Book searchedBookBean2 =  new Book();
        searchedBookBean2.setDescribs(new String[]{"desc2", "desc1", "desc3"});
        //TODO 这里有bug，如不传该字段，因BusBean为int类型，值为0，相当于setPages(0)会查不到。
        searchedBookBean2.setPages(300);
        //查询book2，不带id字段
        System.out.println("============查询book2，不带id字段");
        EasyLuceneService.searchBean(searchedBookBean2,"book");

        Book searchedBookBean3 =  new Book();
        searchedBookBean3.setId("id001");
        searchedBookBean3.setPages(400);
        System.out.println("============更新book1索引");//更新book1索引
        EasyLuceneService.updateBeanIndex(searchedBookBean3,"book");
        System.out.println("============查询更新后的book1");//查询更新后的book1
        EasyLuceneService.searchBean(searchedBookBean3,"book");

        Book deletedBook1 = new Book();
        deletedBook1.setId("id001");
        deletedBook1.setPages(100);
        System.out.println("============按ID删除book1");//按ID删除book1
        EasyLuceneService.deleteBeanIndex(deletedBook1,"book");
        System.out.println("============删除后查询book1");//删除后查询book1
        EasyLuceneService.searchBean(deletedBook1,"book");


        Book deletedBook2 = new Book();
        deletedBook2.setName("bookno2");
        deletedBook2.setPages(300);
        System.out.println("============按非ID删除book2");//按非ID删除book2
        EasyLuceneService.deleteBeanIndex(deletedBook2,"book");
        System.out.println("============删除后查询book2");//删除后查询book2
        EasyLuceneService.searchBean(deletedBook2,"book");
    }
}