package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyFieldBO;
import org.springframework.beans.BeanUtils;

import java.util.Set;

/**
 * Created by magneto on 17-5-17.
 */
public class EntropyFieldVO extends FusionPropertyTypeVO {
    //权值
    private Double entropy;
    //是否被选择
    private Boolean selected;
    //是否唯一列
    private Boolean unique;
    //是否可作为唯一列
    private Boolean canUnique;

    public Double getEntropy() {
        return entropy == null ? -1 : entropy;
    }

    public void setEntropy(Double entropy) {
        this.entropy = entropy;
    }

    public Boolean getSelected() {
        return selected == null ? false : selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getUnique() {
        return unique == null ? false : unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getCanUnique() {
        return canUnique;
    }

    public void setCanUnique(Boolean canUnique) {
        this.canUnique = canUnique;
    }

    public EntropyFieldVO fromBO(EntropyFieldBO entropyFieldBO, Set<Integer> multiMatchPtids) {
        BeanUtils.copyProperties(entropyFieldBO, this);
        super.from(entropyFieldBO.getPropertyType());
        setCanUnique(!multiMatchPtids.contains(getPtid()));
        return this;
    }
}
