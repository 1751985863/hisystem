package com.xgs.hisystem.service;

import com.xgs.hisystem.pojo.bo.BaseResponse;
import com.xgs.hisystem.pojo.vo.PageRspVO;
import com.xgs.hisystem.pojo.vo.drugStorage.DrugReqVO;
import com.xgs.hisystem.pojo.vo.drugStorage.DrugRspVO;
import com.xgs.hisystem.pojo.vo.drugStorage.DrugSearchReqVO;
import com.xgs.hisystem.pojo.vo.drugStorage.UpdateDrugReqVO;

import java.util.List;

/**
 * @author xgs
 * @date 2019-5-12
 * @description:
 */
public interface IDrugStoreService {

    BaseResponse<String> addNewDrug(DrugReqVO reqVO);

    BaseResponse<String> addDrugType(String drugType);

    BaseResponse<String> addEfficacyClassification(String efficacyClassification);

    List<String> getAllDrugType();

    List<String> getAllEfficacyClassification();

    DrugRspVO getDrugInfor(String drug);

    BaseResponse<String> addStorageQuantity(String drug, String addStorageQuantity);

    PageRspVO<DrugRspVO> getAllDrug(DrugSearchReqVO reqVO);

    BaseResponse<String> updateDrug(UpdateDrugReqVO reqVO);

    BaseResponse<String> deleteDrug(String drugName);
}
