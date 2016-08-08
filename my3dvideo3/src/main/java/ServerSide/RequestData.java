package ServerSide;

/**
 * Created by lei on 6/2/16.
 */
public class RequestData {
    public ServerSideAPIKey apiKey;
    public String urlString;

    public RequestData(ServerSideAPIKey apiKey, String urlString)
    {
        this.apiKey = apiKey;
        this.urlString = urlString;
    }
}
