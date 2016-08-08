package ServerSide;

/**
 * Created by lei on 6/2/16.
 */
import org.json.JSONObject;

public class ResponseData {

    public ServerSideAPIKey apiKey;
    public JSONObject data;

    public ResponseData(ServerSideAPIKey apiKey, JSONObject data)
    {
        this.apiKey = apiKey;
        this.data = data;
    }

}
