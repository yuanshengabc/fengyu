package cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sunxingwen on 17-5-20.
 */
public class EntropyTableBO {
    //列属性
    private List<EntropyFieldBO> entropyFields;

    public List<EntropyFieldBO> getEntropyFields() {
        return entropyFields;
    }

    public void setEntropyFields(List<EntropyFieldBO> entropyFields) {
        this.entropyFields = entropyFields;
    }

    public double similarity() {
        BigDecimal s = BigDecimal.ZERO;
        for (EntropyFieldBO entropyFieldBO : entropyFields) {
            s = s.add(new BigDecimal(entropyFieldBO.getEntropy()));
        }

        return s.doubleValue();
    }
}
