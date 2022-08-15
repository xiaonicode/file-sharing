package com.xiaonicode.filesharing.service;

import com.xiaonicode.filesharing.pojo.vo.CatalogVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

/**
 * 目录的业务层接口测试类
 *
 * @author xiaonicode
 * @createTime 2022-08-15
 */
@SpringBootTest
class CatalogServiceTest {

    @Autowired
    CatalogService catalogService;

    @Test
    void listCatalogPaths() {
        CatalogVO[] catalogPaths = catalogService.listCatalogPaths(BigInteger.valueOf(4));
        for (CatalogVO catalogPath : catalogPaths) {
            System.out.println(catalogPath);
        }
    }

}
