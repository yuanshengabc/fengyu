package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next;

import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.Step;

/**
 * Created by magneto on 17-5-22.
 */
public abstract class NextStep extends Step {

    @Override
    protected int changeStep() {
        return ++step;
    }
}
