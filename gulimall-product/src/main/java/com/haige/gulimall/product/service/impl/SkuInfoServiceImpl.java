package com.haige.gulimall.product.service.impl;

import com.google.j2objc.annotations.AutoreleasePool;
import com.haige.gulimall.product.entity.SkuImagesEntity;
import com.haige.gulimall.product.entity.SpuInfoDescEntity;
import com.haige.gulimall.product.service.AttrGroupService;
import com.haige.gulimall.product.service.SkuImagesService;
import com.haige.gulimall.product.service.SpuInfoDescService;
import com.haige.gulimall.product.vo.AttrGroupWithAttrVo;
import com.haige.gulimall.product.vo.SkuItemVo;
import com.haige.gulimall.product.vo.skuinfo.SkuItemSaleAttrVo;
import com.haige.gulimall.product.vo.skuinfo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.SkuInfoDao;
import com.haige.gulimall.product.entity.SkuInfoEntity;
import com.haige.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuImagesService imagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuInfoService skuInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();


        String key = (String) params.get("key");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");

        // 需要用and嵌套
        // (key=1 or key like 'a')
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper -> {
                wrapper.eq("sku_id",key).or().like("sku_name",key);
            }));
        }

        // and
        if(!StringUtils.isEmpty(brandId)){
            queryWrapper.eq("brand_id",brandId);
        }

        if(!StringUtils.isEmpty(catelogId)){
            queryWrapper.eq("catalog_id",catelogId);
        }

        if(!StringUtils.isEmpty(min)){
            queryWrapper.ge("price",min);
        }

        if(!StringUtils.isEmpty(max)){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);
                if(bigDecimal.compareTo(BigDecimal.ZERO) > 0){
                    queryWrapper.le("price",max);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        return this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
    }

    @Autowired
    ThreadPoolExecutor executor;

    /**
     * 异步编排
     * @param skuId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();

        // 异步
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            // 1. sku基本信息 pms_sku_info
            SkuInfoEntity info = this.getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, executor);

        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            // 3. sku对应的spu销售属性组合
            List<SkuItemSaleAttrVo> saleAttrVos = skuInfoService.getSaleAttrValueBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, executor);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            // 4. spu介绍信息 pms_spu_info_desc
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(spuInfoDescEntity);
        }, executor);

        CompletableFuture<Void> groupAttrsFuture = infoFuture.thenAcceptAsync((res) -> {
            // 5. 获取spu规格参数信息
            List<SpuItemAttrGroupVo> spuItemAttrGroupVoList = attrGroupService.getAttrGroupWithAttrsBySpuIdAndCatelogId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroupVoList);
        }, executor);

        // 不需要接收结果，直接异步执行
        CompletableFuture.runAsync(()->{
            // 2. sku图片信息 pms_sku_images
            List<SkuImagesEntity> images = imagesService.getImageBySkuId(skuId);
            skuItemVo.setImages(images);
        },executor);

        // 等待所有任务都完成!
        CompletableFuture.allOf(saleAttrFuture,descFuture,groupAttrsFuture).get();

        return skuItemVo;
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrValueBySpuId(Long spuId) {
        return this.baseMapper.getSaleAttrValueBySpuId(spuId);
    }

}