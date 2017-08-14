package cn.deepclue.datamaster.cleaner.service.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.fusion.EntropyPropertyDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyFieldBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.EntropyPropertyPO;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by magneto on 17-5-23.
 */
@Service("entropyService")
public class EntropyServiceImpl implements EntropyService {
    @Autowired
    FusionWorkspaceDao fusionWorkspaceDao;

    @Autowired
    FusionDao fusionDao;

    @Autowired
    EntropyPropertyDao entropyPropertyDao;

    @Override
    public EntropyTableBO getEntropyTable(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);

        if (fusionWorkspaceBO.getObjectTypeBO() == null) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_INFO_INCOMPLETE);
        }

        RecordSource recordSource = fusionWorkspaceBO.getRecordSource();
        List<FieldEntropy> fieldEntropyList = fusionDao.getFieldEntropies(recordSource);
        Map<String, Double> fieldEntropyMap = new HashMap<>();
        for (FieldEntropy fieldEntropy : fieldEntropyList) {
            fieldEntropyMap.put(fieldEntropy.getFieldName(), fieldEntropy.getEntropy());
        }

        EntropyTableBO entropyTableBO = new EntropyTableBO();
        List<EntropyFieldBO> entropyFieldBOs = new ArrayList<>();
        List<PropertyType> propertyTypes = fusionWorkspaceBO.getObjectTypeBO().getPropertyTypes();
        List<EntropyPropertyPO> entropyPropertyPOs = entropyPropertyDao.getAll(fwsid);
        Set<Integer> selectedPtIds = new HashSet<>();
        Integer uniquedPtId = null;
        for (EntropyPropertyPO entropyPropertyPO : entropyPropertyPOs) {
            selectedPtIds.add(entropyPropertyPO.getPtid());
            if (entropyPropertyPO.getUnique()) {
                uniquedPtId = entropyPropertyPO.getPtid();
            }
        }

        for (PropertyType pt : propertyTypes) {
            EntropyFieldBO entropyFieldBO = new EntropyFieldBO();
            entropyFieldBO.setWsid(fwsid);
            entropyFieldBO.setPropertyType(pt);
            double entropy = fieldEntropyMap.getOrDefault(pt.getName(), 0.0);
            entropyFieldBO.setEntropy(entropy);
            entropyFieldBO.setSelected(selectedPtIds.contains(pt.getPtid()));
            entropyFieldBO.setUnique(uniquedPtId != null && uniquedPtId.equals(pt.getPtid()));

            entropyFieldBOs.add(entropyFieldBO);
        }
        entropyTableBO.setEntropyFields(entropyFieldBOs);

        return entropyTableBO;
    }

    @Override
    public EntropyTableBO getSelectedEntropyTable(int fwsid) {
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        if (fusionWorkspaceBO.getObjectTypeBO() == null) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_INFO_INCOMPLETE);
        }

        RecordSource recordSource = fusionWorkspaceBO.getRecordSource();
        List<FieldEntropy> fieldEntropyList = fusionDao.getFieldEntropies(recordSource);
        Map<String, Double> fieldEntropyMap = new HashMap<>();
        for (FieldEntropy fieldEntropy : fieldEntropyList) {
            fieldEntropyMap.put(fieldEntropy.getFieldName(), fieldEntropy.getEntropy());
        }

        List<EntropyPropertyPO> entropyPropertyPOs = entropyPropertyDao.getAll(fwsid);
        List<PropertyType> propertyTypes = fusionWorkspaceBO.getObjectTypeBO().getPropertyTypes();
        Map<Integer, PropertyType> idPt = new HashMap<>();
        for (PropertyType pt : propertyTypes) {
            idPt.put(pt.getPtid(), pt);
        }

        EntropyTableBO entropyTableBO = new EntropyTableBO();
        List<EntropyFieldBO> entropyFieldBOs = new ArrayList<>();
        for (EntropyPropertyPO entropyPropertyPO : entropyPropertyPOs) {
            Integer sPtid = entropyPropertyPO.getPtid();
            if (!idPt.containsKey(sPtid)) {
                throw new CleanerException(BizErrorEnum.WORKSPACE_INFO_INCOMPLETE);
            }
            String ptName = idPt.get(sPtid).getName();

            EntropyFieldBO entropyFieldBO = new EntropyFieldBO();
            entropyFieldBO.setWsid(fwsid);
            entropyFieldBO.setPropertyType(idPt.get(sPtid));
            double entropy = fieldEntropyMap.getOrDefault(ptName, 0.0);
            entropyFieldBO.setEntropy(entropy);
            entropyFieldBO.setSelected(true);
            entropyFieldBO.setUnique(entropyPropertyPO.getUnique());

            entropyFieldBOs.add(entropyFieldBO);

        }

        entropyTableBO.setEntropyFields(entropyFieldBOs);

        return entropyTableBO;
    }

    @Override
    public boolean deleteEntroipes(RecordSource recordSource) {
        return fusionDao.deleteEntropies(recordSource);
    }

    @Override
    public Integer deleteEntropyTable(int fwsid) {
        return entropyPropertyDao.delete(fwsid);
    }

    @Override
    public boolean insertEntropyTable(List<EntropyPropertyPO> entropyPropertyPOs) {
        entropyPropertyDao.insert(entropyPropertyPOs);
        return true;
    }

    @Override
    public PreviewStats getFusionPreviewInfo(RecordSource recordSource) {
        return fusionDao.getPreviewStats(recordSource);
    }

    @Override
    public Long getSingleFieldFusionNum(RecordSource recordSource) {
        return fusionDao.getSingleFieldFusionNum(recordSource);
    }

    @Override
    public Long getMultiFieldFusionNum(RecordSource recordSource) {
        return fusionDao.getMultiFieldFusionNum(recordSource);
    }

    @Override
    public boolean deleteFusionStore(RecordSource recordSource) {
        return fusionDao.deleteFusionStore(recordSource);
    }
}
