package com.ohgiraffers.section01.javaconfig;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.util.Map;

public class Application {

    /* 중요. 1. (내부) 필드 작성!*/
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost/menudb";
    private static String USER = "ohgiraffers";
    private static String PASSWORD = "ohgiraffers";


    public static void main(String[] args) {

        /* 필기.
        *   DB 접속 관한 환경
        *   -------------------------------
        *   JdbcTransactionFactory : 수동커밋 (좀 더 안전하다. 한번 더 검증할 수 있으니깐~~)
        *   ManagedTransactionFactory : 자동커밋
        *   --------------------------------
        *   PooledDataSource : ConnectionPool 사용
        *   UnPooledDataSource : ConnectionPool 미사용
        * */

        Environment environment = new Environment(
                "dev"                                               // 환경 정보 이름(id) => 키
                , new JdbcTransactionFactory()                      // 트랜젝션 매니저 종류 결졍(JDBC or MANAGED)
                , new PooledDataSource(DRIVER,URL, USER, PASSWORD)  // ConnectionPool 사용 유무(Pooled or UnPooled)
        );

        /* 필기. 생성한 환경 정보로 MyBatis 설정 객체 생성 */
        Configuration configuration = new Configuration(environment);   // 위에서 환경설정을 만들어주고, ()에 넣는다!

        // 인터페이스와 연결시켜주려는 것(addmapper =dao같은~!)만들어줘야함! Mapper 의 정보를 클래스와 연결~~!!
        configuration.addMapper(Mapper.class);

        /* 필기. ▽ 인터페이스이기 떄문에 도와줄 설정요소들이 필요하다! (3가지)
        *   SqlSessionFactory : SqlSession 객체를 생성하기 위한 팩토리_(공장) 역할의 인터페이스
        *   SqlSessionFactoryBuilder : SqlSessionFactory 인터페이스 타입의 하위 구현 객체를 생성하기 위한 빌드 역할_(인부들)
        *   build() : 환경 설정에 대한 정보를 담고 있는 Configuration 타입의 객체 혹은 외부 설정 파일과 연결 된 Stream
        *             을 매개변수로 전달하면 SqlSessionFactory 인터페이스 타입의 객체를 반환하는 메소드
        * */
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        // 중요! ▲ 인터페이스라서 인부들의 도움이 필요하다!(build) ▲

        /* 필기.
        *   openSession() : SqlSession 인터페이스 타입의 객체를 반환하는 메소드로 boolean 타입을 인자로 전달
        *   - false : Connection 인터페이스 타입 객체로 DML(insert, update, delete) 수행 후
        *             auto commit 옵션 *false(권장)
        * */
        SqlSession sqlSession = sqlSessionFactory.openSession(false);   // 내부에 값 전달할 수 있다.

        /* 필기. getMapper() : Configuration 에 등록 된 메퍼를 동일 타입에 대해 반환하는 메소드 */
        // Mapper 인터페이스 실행! 위에 식 미리 만들어줌.
        Mapper mapper = sqlSession.getMapper(Mapper.class);

        // 리터럴 형태이므로(현재기준: 2024-03-11) 담아주기!>> java.util.Date date= ~~  로!
        java.util.Date date = mapper.selectSysdate();
        System.out.println("date = " + date);
        sqlSession.close();     // 열어줬으니 닫아주기~!

    }
}
