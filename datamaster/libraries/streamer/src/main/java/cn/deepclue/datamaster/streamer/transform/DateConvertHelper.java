package cn.deepclue.datamaster.streamer.transform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lilei-mac on 2017/4/28.
 */
public class DateConvertHelper {

    public static final Long TIME_MILLIS_THRESHOLD = 1000000000L;

    private static final String[] DATE_PARTS = new String[]{
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "yyyy MM dd",
            "yyyy.MM.dd",
            "yyyy年MM月dd日",
            "MM-dd-yyyy",
            "MM/dd/yyyy",
            "MM dd yyyy",
            "MM.dd.yyyy",
            "MM月dd日yyyy年",
            "dd-MM-yyyy",
            "dd/MM/yyyy",
            "dd MM yyyy",
            "dd.MM.yyyy",
            "dd日MM月yyyy年",
            "yyyyMMdd",
            "MMddyyyy",
            "ddMMyyyy"
    };

    private static String[] TIME_PARTS = new String[]{
            "HH:mm:ss S",
            "HH:mm:ss",
            "HH时mm分ss秒S毫秒",
            "HH时mm分ss秒",
            "hh:mm:ss a",
            "hh:mm:ssa",
            "hh时mm分ss秒 a",
            "hh时mm分ss秒a",
            "HH:mm:ss:S",
            "HH:mm:ss.S",
            "hh:mm:ss S a",
            "hh:mm:ss:S a",
            "hh:mm:ss.S a",
            "hh.mm.ss:S a",
            "hh.mm.ss.S a",
            "hh:mm:ss:Sa",
            "hh:mm:ss.Sa",
            "hh:mm a",
            "HHmmss",
            "HH:mm",
            "HHmm",

            "hh:mm:ss a z",
            "hh:mm:ssa z",
            "hh:mm:ss:S a z",
            "hh:mm:ss.S a z",
            "hh.mm.ss:S a z",
            "hh.mm.ss.S a z",
            "HH:mm:ss:S z",
            "HH:mm:ss.S z",
            "HH.mm.ss:S z",
            "HH.mm.ss.S z",
            "hh:mm:ss:S a z",
            "hh:mm:ss.S a z",
            "hh.mm.ss:S a z",
            "hh.mm.ss.S a z",
            "HH:mm:ss z",
            "hh.mm a z",
            "HH.mm z",

            "hh:mm:ss az",
            "hh:mm:ssaz",
            "hh:mm:ss:S az",
            "hh:mm:ss.S az",
            "hh.mm.ss:S az",
            "hh.mm.ss.S az",
            "hh:mm:ss:Saz",
            "hh:mm:ss.Saz",
            "hh.mm.ss:Saz",
            "hh.mm.ss.Saz",
            "HH:mm:ss:Sz",
            "HH:mm:ss.Sz",
            "HH.mm.ss:Sz",
            "HH.mm.ss.Sz",
            "HH:mm:ssz",
            "hh.mm az",
            "HH.mmz",

            ""
    };

    private static String[] SOME_STAND = new String[]{
            "E M d HH:mm:ss yyyy",
            "E,d M yyyy HH:mm:ss z",
            "yyyy d-M hh.mm.ss S a"
    };

    public static Date parseFormat(String format,String text) throws ParseException{

        return new SimpleDateFormat(format).parse(text);
    }

    public static Date hitParseFormat(List<String> cacheDateFormat,String text){

        Date resultDate;
        for(String someStand:SOME_STAND){
            try {
                resultDate = parseFormat(someStand,text);
                cacheDateFormat.add(someStand);
                return resultDate;
            } catch (ParseException ignored){

            }
        }
        for(String datePart:DATE_PARTS){
            for(String timePart:TIME_PARTS){
                StringBuilder formatLocal = new StringBuilder();
                formatLocal.append(datePart);
                formatLocal.append(timePart);
                try {
                    resultDate = parseFormat(formatLocal.toString(),text);
                    cacheDateFormat.add(formatLocal.toString());
                    return resultDate;
                } catch (ParseException ignored){

                }
            }
        }
        return null;
    }
}
