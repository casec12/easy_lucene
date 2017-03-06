EasyLucene是一个Bean<->Index快速转换，索引、搜索的Lucene工具包。
基于Lucene6.4.1版本开发，java8+以上环境运行

使用方法：
1. 创建lucene索引，可使用shelltools工具包的run.bat/run.sh
2. 配置lucene_config.properties，该文件配置lucene文件索引位置
3. 与项目集成后，添加Bean转换的配置，例子中com.cncell.bean中的Book、with_prototype.easy_lucene.bean.DocBook为一对Bean转换，将需要索引的字段添加到DocBean中.
4. 配置Bean映射的特殊关系，修改文件with_prototype.easy_lucene.config.Constants类,其中：
   a. busbean_lucenebean_mapping:BusBean与DocBean的映射关系
   b. doc_type_mapping:暂时没用
   c. bean_id_mapping:BusBean的ID配置，与Bean的主键关系一致
   d. none_segment_mapping:BusBean不分词字段设置
5. 运行with_prototype.easy_lucene.example.Example例子，例子中包含了所有的服务方法调用。记录索引、更新索引、删除索引、索引查询
