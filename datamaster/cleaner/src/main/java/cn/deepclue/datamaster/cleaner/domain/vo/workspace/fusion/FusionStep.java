package cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion;

/**
 * Created by magneto on 17-5-15.
 * ONTOLOGY_SELECTION --- 选择业务模型
 * DATASOURCE_SELECTION --- 选择数据源
 * FUSION_MODE --- 选择融合模式
 * FUSION --- 开始融合
 */
public enum FusionStep {
    ONTOLOGY_SELECTION(0), DATASOURCE_SELECTION(1), FUSION_MODE(2), FUSION(3);

    private int step;

    FusionStep(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public static FusionStep getFusionStep(int step) {
        switch (step) {
            case 0:
                return ONTOLOGY_SELECTION;
            case 1:
                return DATASOURCE_SELECTION;
            case 2:
                return FUSION_MODE;
            case 3:
                return FUSION;
            default:
                throw new IllegalStateException("Unknown fusion step: " + step);
        }
    }
}
