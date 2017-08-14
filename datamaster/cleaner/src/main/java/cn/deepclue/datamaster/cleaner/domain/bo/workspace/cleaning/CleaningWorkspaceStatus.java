package cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning;

/**
 * IMPORTING --- 导入任务进行中
 * ANALYZING, --- 分析任务进行中
 * TRANSFORMING, --- 清洗任务进行中
 * EXPORTING, --- 导出任务进行中
 * IMPORT_FAILED, --- 导入任务失败
 * ANALYSIS_FAILED --- 分析任务失败
 * TRANSFORM_FAILED --- 清洗任务失败
 * EXPORT_FAILD, --- 导出任务失败
 * IDLE, --- 无任务运行
 * FINISHED; ---清洗任务结束
 * Created by xuzb on 17/03/2017.
 */
public enum CleaningWorkspaceStatus {
    IMPORTING(0), ANALYZING(1), TRANSFORMING(2), EXPORTING(3),
    IMPORT_FAILED(4), ANALYSIS_FAILED(5), TRANSFORM_FAILED(6), EXPORT_FAILD(7),
    IDLE(8), FINISHED(9);

    private int value;
    CleaningWorkspaceStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static CleaningWorkspaceStatus getWorkspaceStatus(int value) {
        switch (value) {
            case 0:
                return IMPORTING; // 正在清洗中
            case 1:
                return ANALYZING;
            case 2:
                return TRANSFORMING;
            case 3:
                return EXPORTING;
            case 4:
                return IMPORT_FAILED;
            case 5:
                return ANALYSIS_FAILED;
            case 6:
                return TRANSFORM_FAILED;
            case 7:
                return EXPORT_FAILD;
            case 8:
                return IDLE;
            case 9:
                return FINISHED;

            default:
                throw new IllegalStateException("Unknown workspace status: " + value);
        }
    }
}
