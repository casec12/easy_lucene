package othertest;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Created by chenzhaolei on 2017/2/24.
 */
public class BeanUtilTest2 {


    public static void main(String[] args) throws Exception {
        //1. create init bean , print
        PersonBean person = new BeanUtilTest2().new PersonBean();

        person.setName("harry");
        person.setAge(10);
        person.setSex("male");
        System.out.println(person.toString());

        //2. use bean util to change value
        PropertyUtils.setProperty(person, "name", "black");
        PropertyUtils.setProperty(person, "age", 10);
        PropertyUtils.setProperty(person, "sex", "famale");

        //3. use bean util to print
        String nameValue = (String) PropertyUtils.getProperty(person, "name");
        int ageValue = (Integer) PropertyUtils.getProperty(person, "age");
        String sexValue = (String) PropertyUtils.getProperty(person, "sex");
        System.out.println(nameValue+" "+ageValue+" "+sexValue);


        char a = 46;
        char b = 40;
        char c = 91;
        System.out.println(a+" "+b+" "+c);

    }

    class PersonBean {
        public PersonBean() {
        }
        private String name ;
        private String sex;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "PersonBean{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
