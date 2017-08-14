package cn.deepclue.datamaster.streamer.transform.filter;

import java.util.Date;

/**
 * Created by xuzb on 09/04/2017.
 */
public class DateFilter extends SingleFieldFilter {

    private Date endDate;
    private Date startDate;

    public DateFilter(String sourceFieldName) {
        super(sourceFieldName);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    protected boolean acceptValue(Object value) {
        if (value instanceof Date) {
            Date dateValue = (Date) value;
            if (endDate != null && startDate != null) {
                return dateValue.after(startDate) && dateValue.before(endDate);
            } else if (endDate != null) {
                return dateValue.before(endDate);
            } else if (startDate != null) {
                return dateValue.after(startDate);
            } else {
                throw new IllegalStateException("Start date or end date must be set in date filter.");
            }
        }

        return false;
    }
}
