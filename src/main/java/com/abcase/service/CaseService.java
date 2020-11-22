package com.abcase.service;

import com.abcase.entity.Case;
import com.abcase.mapper.CaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CaseService {
    @Autowired
    CaseMapper caseMapper;

    /**
     * 查询用例
     *
     * @param caseInfo
     * @return
     */
    public List<Case> queryCase(Case caseInfo) {
        return caseMapper.selectCase(caseInfo);
    }

    /**
     * 插入用例
     *
     * @param caseInfo
     * @return
     */
    public int insertUser(Case caseInfo) {
        return caseMapper.insertUser(caseInfo);
    }

    public void getCaseInfo(Case caseInfo, File interfaceFile, File caseFile) {
        CaseGenService genService = new CaseGenService();
        genService.interfaceCaseGen(caseInfo, interfaceFile, caseFile);
    }
}
