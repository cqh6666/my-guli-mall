package com.haige.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haige.gulimall.product.entity.BrandEntity;
import com.haige.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("ios15");
        brandEntity.setName("苹果");
        // 插入数据
//        brandService.save(brandEntity);
//        System.out.println("保存成功！");
//
//        // 更新数据
//        brandService.updateById(brandEntity);

        // 查询
        List<BrandEntity> brandList = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", "1L"));
        brandList.forEach((item) -> {
            System.out.println(item);
        });
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void redisTest(){
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        // 保存
        opsForValue.set("haige","hello"+ UUID.randomUUID().toString());

        String haige = opsForValue.get("haige");
        System.out.println(haige);

    }

}
