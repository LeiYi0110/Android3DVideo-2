package ServerSide;

/**
 * Created by lei on 6/2/16.
 */

import java.net.URLEncoder;
import java.util.List;


public class ServerSideURL {

    private static String baseURL = "http://www.3dtm.net.cn:8088/";

    public static String videoList()
    {
        return baseURL + "videoList";

    }

    public static String searchKeyValue()
    {
        return baseURL + "searchKeyValue";
    }
    public static String setCheckNum(String checkNum)
    {
        return baseURL + "setCheckNum/" + (checkNum.length() == 0? "a":checkNum);
    }

    public static String test(){
        return  "http://www.xiangyouji.com.cn:3000/area";
    }

}
