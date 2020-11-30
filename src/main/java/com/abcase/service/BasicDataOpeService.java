package com.abchina.autotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础数据操作Service
 * by llh
 */
@Service
public class BasicDataOpeService {
    @Autowired
    private ParaGenerate paraGenerate;

    /**
     * 字符串基础数据生成
     * @throws Exception
     */
    public void BasicStringDataGenerate()throws Exception{
        try{

        }catch(Exception e){
            throw e;
        }
    }

    /**
     * 数型基础数据生成
     * @throws Exception
     */
    public void BasicNumberDataGenerate()throws Exception{
        try{

        }catch(Exception e){
            throw e;
        }
    }
}
