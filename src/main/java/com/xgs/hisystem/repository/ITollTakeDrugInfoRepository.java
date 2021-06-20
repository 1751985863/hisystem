package com.xgs.hisystem.repository;

import com.xgs.hisystem.pojo.entity.TollTakeDrugInfoEntity;

import java.util.List;

/**
 * @author XueGuiSheng
 * @date 2020/3/10
 * @description:
 */
public interface ITollTakeDrugInfoRepository extends BaseRepository<TollTakeDrugInfoEntity>{

    /**
     * 根据患者Id与取药状态查询
     */
    List<TollTakeDrugInfoEntity> findByPatientIdAndTakingDrugStatus(String patientId, int takingDrugStatus);

    /**
     * 根据处方号与取药状态查询
     */
    TollTakeDrugInfoEntity findByPrescriptionNumAndTakingDrugStatus(String prescriptionNum,int takingDrugStatus);


    TollTakeDrugInfoEntity findByPrescriptionNum(String prescriptionNum);
}
