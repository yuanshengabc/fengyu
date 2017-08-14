package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyFieldBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by magneto on 17-5-17.
 */
public class EntropyTableVO {
    //业务模型id
    private int otid;
    //业务模型名称
    private String name;
    //属性信息
    private List<EntropyFieldVO> propertyTypes;

    public int getOtid() {
        return otid;
    }

    public void setOtid(int otid) {
        this.otid = otid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EntropyFieldVO> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(List<EntropyFieldVO> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public EntropyTableVO fromBO(ObjectTypeBO objectTypeBO, EntropyTableBO entropyTableBO, Set<Integer> multiMatchPtids) {
        BeanUtils.copyProperties(objectTypeBO, this);
        List<EntropyFieldVO> entropyFieldVOs = new ArrayList<>();
        for (EntropyFieldBO entropyFieldBO : entropyTableBO.getEntropyFields()) {
            EntropyFieldVO entropyFieldVO = new EntropyFieldVO().fromBO(entropyFieldBO, multiMatchPtids);
            entropyFieldVOs.add(entropyFieldVO);
        }
        this.setPropertyTypes(entropyFieldVOs);
        return this;
    }
}
