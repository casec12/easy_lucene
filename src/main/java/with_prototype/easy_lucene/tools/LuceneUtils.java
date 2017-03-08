package with_prototype.easy_lucene.tools;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import with_prototype.easy_lucene.bean.BaseBean;
import with_prototype.easy_lucene.bean.DocBook;
import with_prototype.easy_lucene.config.Config;
import with_prototype.easy_lucene.config.Constants;
import with_prototype.easy_lucene.exception.EasyLuceneException;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static with_prototype.easy_lucene.constants.ExceptionEnum.*;

public class LuceneUtils {

    public static <T extends BaseBean> void updateBeanIndex(T docBean) throws Exception {
        IndexWriter idxWriter = new IndexWriter(Config.getDefaultDirectory(), new IndexWriterConfig(Config.getDefaultAnalyzerr()));
        updateBean(idxWriter, docBean);
    }

    private static <T extends BaseBean> void checkBaseBean(T docBean) {
        String docId = docBean.getDocId();
        String docType = docBean.getDocType();
        if (docId == null || "".equals(docId))
            throw new IllegalArgumentException(NULL_DOCBEAN_ID_EXCEPTION.getAllInfo());
        if (docType == null || "".equals(docType))
            throw new IllegalArgumentException(ILLEGAL_DOCTYPE_ARGUMENT_EXCEPTION.getAllInfo());
    }

    private static <T extends BaseBean> void updateBean(IndexWriter indexWriter, T docBean) throws Exception {
        checkBaseBean(docBean);

        String docId = docBean.getDocId();
        String docType = docBean.getDocType();

        Document luceneDoc = new Document();
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(docBean);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String fieldName = propertyDescriptor.getName();
            if (Constants.CLASS_FIELD_NAME.equals(fieldName))
                continue;

            boolean canRead = PropertyUtils.isReadable(docBean, fieldName);
            if (!canRead)
                continue;

            //docId，docType不分词
            Object field = propertyDescriptor.getReadMethod().invoke(docBean);
            if (fieldName.equals(Constants.DOC_ID_FIELD) || fieldName.equals(Constants.DOC_TYPE_FIELD)) {
                luceneDoc.add(new StringField(fieldName, field.toString(), Field.Store.YES));
                luceneDoc.add(new SortedDocValuesField(fieldName, new BytesRef(fieldName.toString())));
                continue;
            }

            if (field == null)
                continue;

            //分词标志
            boolean needSegment = false;
            Class propRunTypeClass = docBean.getClass();
            for (Map.Entry<Class, String[]> KV : Config.getNeed_segment_mapping().entrySet()) {
                List fieldsNameList = Arrays.asList(KV.getValue());
                if (propRunTypeClass.equals(KV.getKey()) && fieldsNameList.contains(fieldName)) {
                    needSegment = true;
                    break;
                }
            }

            String indexedFieldName = "";
            String indexedFieldValue = "";
            if (field instanceof String[]) {
                String fieldsStr = "";
                String[] sorted = (String[]) field;
                Arrays.sort(sorted);
                for (int count = 0; count < ((String[]) field).length; count++) {
                    if (count != 0)
                        fieldsStr += Config.getArrayPropTransSeprator();
                    fieldsStr += ((String[]) field)[count];
                }
                indexedFieldName = fieldName;
                indexedFieldValue = fieldsStr.toString();
            } else {
                indexedFieldName = fieldName;
                indexedFieldValue = field.toString();
            }
            if (needSegment) {
                luceneDoc.add(new TextField(indexedFieldName, indexedFieldValue, Field.Store.YES));
            } else {
                luceneDoc.add(new StringField(indexedFieldName, indexedFieldValue, Field.Store.YES));
            }
        }

/* old code
        Map<String, String> strDocBeanMap = BeanUtils.describe(docBean);
        Document luceneDoc = new Document();
        for (Map.Entry<String, String> entry : strDocBeanMap.entrySet()) {
            String beanField = entry.getKey();
            String beanValue = entry.getValue();

            if ("class".equals(beanField))
                continue;
            if (beanValue == null || "".equals(beanValue))
                continue;

            if ("docId".equals(beanField) || "docType".equals(beanField)) {
                luceneDoc.add(new StringField(beanField, beanValue, Field.Store.YES));
                luceneDoc.add(new SortedDocValuesField(beanField, new BytesRef(beanValue)));
            } else {
                luceneDoc.add(new TextField(beanField, beanValue, Field.Store.YES));
                luceneDoc.add(new SortedDocValuesField(beanField, new BytesRef(beanValue)));
            }
        }
*/
        //searchBean，用于查询的docBean，只有docType和docId两个值
        T docBeanSearched = (T) docBean.getClass().newInstance();
        docBeanSearched.setDocType(docType);
        docBeanSearched.setDocId(docId);
        Document document = findBeanIndex(docBeanSearched);

        if (document != null) {//beanid in index exists, update index
            //TODO 这里，要把docType也作为类似主键的标志，加入Term
            indexWriter.updateDocument(new Term(Constants.DOC_ID_FIELD, docId), luceneDoc);
            indexWriter.commit();
        } else {//beanid in index not exists, add index
            indexWriter.addDocument(luceneDoc);
            indexWriter.commit();
        }
        indexWriter.close();
/*老的 Demo处理逻辑
        if (docBean instanceof DocBook) {
            DocBook docBook = (DocBook) docBean;
            String documentID = docBook.getDocId();
            String documentType = docBook.getDocType();
            String id = docBook.getId();
            String name = docBook.getName();

            Document doc = new Document();
            doc.add(new TextField("documentID", documentID, Field.Store.YES));

            DocBook documentBookById = new DocBook();
            documentBookById.setDocId(documentID);

            Document document1 = findBeanIndex(documentBookById);
            if (document1 != null) {//beanid in index exists, update index
                doc.add(new TextField("documentId", documentID, Field.Store.YES));
                doc.add(new TextField("documentType", documentType, Field.Store.YES));
                doc.add(new TextField("id", id, Field.Store.YES));
                doc.add(new TextField("name", name, Field.Store.YES));
                w.updateDocument(new Term("documentID", documentID), doc);
            } else {//beanid in index not exists, add index
                doc.add(new TextField("documentId", documentID, Field.Store.YES));
                doc.add(new TextField("documentType", documentType, Field.Store.YES));
                doc.add(new TextField("id", id, Field.Store.YES));
                doc.add(new TextField("name", name, Field.Store.YES));
                w.addDocument(doc);
            }
        }
*/
    }

    public static <T extends BaseBean> List searchBean(T docBean) throws ParseException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        checkBaseBean(docBean);
        Class docBeanClass = docBean.getClass();

        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(Config.getDefaultDirectory());
        IndexSearcher searcher = new IndexSearcher(reader);
        List searchList = new ArrayList();

        Map<String, String> docBeanMap = BeanUtils.describe(docBean);
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(docBean);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            //先看名称，在看类型，最后放到Document中
            String fieldName = propertyDescriptor.getName();
            if (Constants.CLASS_FIELD_NAME.equals(fieldName))
                continue;

            boolean canRead = PropertyUtils.isReadable(docBean, fieldName);
            if (!canRead)
                continue;

            Object field = propertyDescriptor.getReadMethod().invoke(docBean);
            if (field == null)
                continue;

            if (field instanceof String[]) {
                String[] sorted = (String[]) field;
                Arrays.sort(sorted);
                String fieldsStr = "";
                for (int count = 0; count < (sorted).length; count++) {
                    if (count != 0)
                        fieldsStr += Config.getArrayPropTransSeprator();
                    fieldsStr += (sorted)[count];
                }
                docBeanMap.put(fieldName, fieldsStr.toString());
            } else {
                docBeanMap.put(fieldName, field.toString());
            }
        }
        Map<Class, Class> busbean_lucenebean_mapping = Config.getBusbean_lucenebean_mapping();
        Class mapping_busbean_class = null;
        for (Map.Entry<Class, Class> KV : busbean_lucenebean_mapping.entrySet()) {
            Class busBeanClass = KV.getKey();
            Class luceneBeanClass = KV.getValue();
            if (luceneBeanClass.equals(docBeanClass)) {
                mapping_busbean_class = busBeanClass;
                break;
            }
        }

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        int hasEfectiveQuery = 0;//是否包含有效的查询条件
        for (Map.Entry<String, String> entry : docBeanMap.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            if (fieldName.equals(Constants.CLASS_FIELD_NAME))
                continue;

            if (fieldValue == null || "".equals(fieldValue))
                continue;

            if (fieldName.equals(Constants.DOC_ID_FIELD))
                continue;

            if (mapping_busbean_class != null && Arrays.asList(Config.getBean_id_mapping().get(mapping_busbean_class)).contains(fieldName))
                continue;

            //TODO 对于配置的分词字段，使用默认器生成Query，并添加到Builder中
            for(Map.Entry<Class,String[]> entry_ : Config.getNeed_segment_mapping().entrySet()){
                List segementFieldNames = Arrays.asList(entry_.getValue());
                if(docBeanClass.equals(entry_.getKey()) && segementFieldNames.contains(fieldName)){
                    queryBuilder.add(new QueryParser(fieldName, Config.getDefaultAnalyzerr()).parse(fieldValue), BooleanClause.Occur.MUST);
                }else{
                    queryBuilder.add(new TermQuery(new Term(fieldName, fieldValue)), BooleanClause.Occur.MUST);
                }
            }
            hasEfectiveQuery++;
        }

        if (hasEfectiveQuery == 0)
            throw new EasyLuceneException(BEAN_FIELD_ALL_NULL_EXCEPTION.getAllInfo());

        Query q = queryBuilder.build();

        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId1 = hits[i].doc;
            Document d = searcher.doc(docId1);
            T searchRow = (T) docBean.getClass().newInstance();
            System.out.print(i + ". ");
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String fieldName = propertyDescriptor.getName();
                if (Constants.CLASS_FIELD_NAME.equals(fieldName))
                    continue;
                Class fieldClass = propertyDescriptor.getReadMethod().getReturnType();
                if (fieldClass.equals(String[].class)) {
                    String[] strArr = (d.get(fieldName) == null) ? null : d.get(fieldName).split(Config.getArrayPropTransSeprator());
                    propertyDescriptor.getWriteMethod().invoke(searchRow, (Object) strArr);
                } else {
                    propertyDescriptor.getWriteMethod().invoke(searchRow, d.get(fieldName));
                }
                System.out.print(fieldName + "=" + d.get(fieldName) + " ");
            }
            System.out.println();
            searchList.add(searchRow);
        }
        reader.close();
        return searchList;
/* old code
        // 2. query
        if (docBean instanceof DocBook) {
            DocBook documentBook = (DocBook) docBean;
            String documentType = documentBook.getDocType();
            String id = documentBook.getId();
            String name = documentBook.getName();
            Map<String, String> searchedDataMap = new HashMap<String, String>();
            if (documentType != null) {
                searchedDataMap.put("documentType", documentType);
            }
            if (id != null) {
                searchedDataMap.put("id", id);
            }
            if (name != null) {
                searchedDataMap.put("name", name);
            }
            String[] queries1 = new String[searchedDataMap.size()];
            String[] fields1 = new String[searchedDataMap.size()];
            Iterator it = searchedDataMap.keySet().iterator();
            int arrIndexNo = 0;
            while (it.hasNext()) {
                String fieldName = (String) it.next();
                String fieldValue = searchedDataMap.get(fieldName);
                queries1[arrIndexNo] = fieldValue;
                fields1[arrIndexNo] = fieldName;
                arrIndexNo++;
            }
            // 3. search
            Query q = new MultiFieldQueryParser(fields1, analyzer).parse(queries1, fields1, analyzer);
            TopScoreDocCollector collector1 = TopScoreDocCollector.create(hitsPerPage);
            searcher.search(q, hitsPerPage);
            ScoreDoc[] hits1 = collector1.topDocs().scoreDocs;
            // 4. display results
            System.out.println("Found " + hits1.length + " hits.");

            for (int i = 0; i < hits1.length; ++i) {
                int docId1 = hits1[i].doc;
                Document d = searcher.doc(docId1);
                System.out.println((i + 1) + ". " + d.get("documentId") + "\t" + d.get("id") + "\t" + d.get("name"));
                DocBook searchRow = new DocBook();
                searchRow.setDocId(d.get("documentId"));
                searchRow.setDocType(d.get("documentType"));
                searchRow.setId(d.get("id"));
                searchRow.setName(d.get("name"));
                searchList.add(searchRow);
            }
        }
        reader.close();
        return searchList;
*/
    }

    public static <T extends BaseBean> Document findBeanIndex(T docBean) throws Exception {
        checkBaseBean(docBean);

        String docId = docBean.getDocId();

        int hitsPerPage = 1;
        IndexReader reader = DirectoryReader.open(Config.getDefaultDirectory());
        IndexSearcher searcher = new IndexSearcher(reader);

        //TODO 这里要调整为还支持DocType的Parser
        Query q = new TermQuery(new Term(Constants.DOC_ID_FIELD, docId));

        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        if (hits.length > 0) {
            int docId_ = hits[0].doc;
            Document document = searcher.doc(docId_);
            //TODO 关闭资源需要考虑reader关闭前出现异常的情况
            reader.close();
            return document;
        } else {
            //TODO 关闭资源需要考虑reader关闭前出现异常的情况
            reader.close();
            return null;
        }

/* old code
        if (docBean instanceof DocBook) {
            DocBook documentBook = (DocBook) docBean;
            String documentId = documentBook.getDocId();

            Query q = new QueryParser("documentId", analyzer).parse(documentId);
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 0) {
                int docId = hits[0].doc;
                Document document = searcher.doc(docId);
                return document;
            } else {
                return null;
            }
        }
        return null;
*/
    }

    public static <T extends BaseBean> void deleteBeanIndex(T docBean) throws Exception {
        checkBaseBean(docBean);
        String docId = docBean.getDocId();
        String docType = docBean.getDocType();


        // 1. create the index
        IndexWriter indexWriter = new IndexWriter(Config.getDefaultDirectory(), new IndexWriterConfig(Config.getDefaultAnalyzerr()));

        T searchedDocBean = (T) docBean.getClass().newInstance();
        searchedDocBean.setDocType(docType);
        searchedDocBean.setDocId(docId);

        DocBook documentBookById = new DocBook();
        documentBookById.setDocId(docId);

        Document document = findBeanIndex(searchedDocBean);
        if (document != null) {//beanid in index exists, update index
            //TODO 这里要加上DocType
            indexWriter.deleteDocuments(new Term(Constants.DOC_ID_FIELD, docId));
            indexWriter.commit();
        }
        indexWriter.close();

/* old code
        if (docBean instanceof DocBook) {
            DocBook documentBook = (DocBook) docBean;
            String documentID = documentBook.getDocId();

            DocBook documentBookById = new DocBook();
            documentBookById.setDocId(documentID);

            Document document = findBeanIndex(documentBookById);
            if (document != null) {//beanid in index exists, update index
                w.deleteDocuments(new Term("documentID", documentID));
            }
        }
*/
    }


}
