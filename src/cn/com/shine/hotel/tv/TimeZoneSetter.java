package cn.com.shine.hotel.tv;

import java.util.Calendar;
import java.util.TimeZone;
import android.content.Context;
import android.util.Log;
import cn.com.shine.hotel.impl.CommonDeskImpl;
import cn.com.shine.hotel.service.CommonDesk;

/*import cn.com.shine.hospital.tvsettingservice.CommonDesk;
import cn.com.shine.hospital.tvsservicefun.CommonDeskImpl;
*/
import com.tvos.common.vo.TvOsType.EnumTimeZone;

public class TimeZoneSetter {

    private final String TAG = "TimeZoneSetter";
    private final int HOURSECOND = 60 * 60000;
    private CommonDesk commonDesk = null;

    public TimeZoneSetter(Context context) {
        commonDesk = CommonDeskImpl.getInstance();
    }

    public void updateTimeZone() {
        Log.e(TAG, "================>>>> getMinuteOffset() = " + getMinuteOffset());
        Log.e(TAG, "================>>>> getHourOffset() = " + getHourOffset());
        if (getMinuteOffset() == 0) {
            switch (getHourOffset()) {
                case -11:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS11_START, true);
                break;
                case -10:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS10_START, true);
                break;
                case -9:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS9_START, true);
                break;
                case -8:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS8_START, true);
                break;
                case -7:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS7_START, true);
                break;
                case -6:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS6_START, true);
                break;
                case -5:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS5_START, true);
                break;
                case -4:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS4_START, true);
                break;
                case -3:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS3_START, true);
                break;
                case -2:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS2_START, true);
                break;
                case -1:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS1_START, true);
                break;
                case 0:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_0_START, true);
                break;
                case 1:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_1_START, true);
                break;
                case 2:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_2_START, true);
                break;
                case 3:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_3_START, true);
                break;
                case 4:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_4_START, true);
                break;
                case 5:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_5_START, true);
                break;
                case 6:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_6_START, true);
                break;
                case 7:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_7_START, true);
                break;
                case 8:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_8_START, true);
                break;
                case 9:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_9_START, true);
                break;
                case 10:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_10_START, true);
                break;
                case 11:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_11_START, true);
                break;
                case 12:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_12_START, true);
                break;
                case 13:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_13_START, true);
                break;
                default:
                    Log.e(TAG, "==============>>>> ooooh~~~~, set time zone fail!");
                break;
            }
        }
        else if (getMinuteOffset() == 30) {
            switch (getHourOffset()) {
                case -3:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_MINUS3_5_START, true);
                break;
                case 3:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_3POINT5_START, true);
                break;
                case 4:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_4POINT5_START, true);
                break;
                case 5:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_5POINT5_START, true);
                break;
                case 6:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_6POINT5_START, true);
                break;
                case 9:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_9POINT5_START, true);
                break;
                case 10:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_10POINT5_START, true);
                break;
                default:
                    Log.e(TAG, "==============>>>> ooooh~~~~, set time zone fail!");
                break;
            }
        }
        else if (getMinuteOffset() == 45) {
            switch (getHourOffset()) {
                case 5:
                    commonDesk.setTimeZone(EnumTimeZone.E_TIMEZONE_GMT_5POINT45_START, true);
                break;
                default:
                    Log.e(TAG, "==============>>>> ooooh~~~~, set time zone fail!");
                break;
            }
        }
    }

    private int getHourOffset() {
        String id = TimeZone.getDefault().getID();
        long date = Calendar.getInstance().getTimeInMillis();
        TimeZone tz = TimeZone.getTimeZone(id);
        int offset = tz.getOffset(date);
        int houroffset = offset / HOURSECOND;
        return houroffset;
    }

    private int getMinuteOffset() {
        String id = TimeZone.getDefault().getID();
        long date = Calendar.getInstance().getTimeInMillis();
        TimeZone tz = TimeZone.getTimeZone(id);
        int offset = tz.getOffset(date);
        int minoffset = offset / 60000;
        minoffset %= 60;
        return minoffset;
    }
}
