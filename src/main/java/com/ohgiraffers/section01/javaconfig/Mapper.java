package com.ohgiraffers.section01.javaconfig;

import org.apache.ibatis.annotations.Select;

public interface Mapper {

    @Select("SELECT CURDATE() FROM DUAL ")
    java.util.Date selectSysdate();         // 위에 커리문이 동작할 수 있도록 작성함

}

