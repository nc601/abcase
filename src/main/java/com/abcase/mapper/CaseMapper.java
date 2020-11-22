package com.abcase.mapper;

import com.abcase.entity.Case;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseMapper {
    List<Case> selectCase(Case caseInfo);

    int insertUser(Case user);

    void updateUser(Case user);

    List<String> queryTeams();

    List<String> queryTeamMembers(String teamName);
}
