package cn.deepclue.datamaster.cleaner.service.fusion.impl.step;

/**
 * Created by magneto on 17-5-22.
 */
public abstract class Step {
    protected int step;
    protected abstract int changeStep();

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
