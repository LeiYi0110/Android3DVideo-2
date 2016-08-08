package ServerSide;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lei on 6/7/16.
 */


public class DownloadWebpageTask extends AsyncTask<RequestData, Void, ResponseData> {

    public FinishGetDataListener mCallbac;

    public interface FinishGetDataListener {
        public boolean didGetData(ResponseData data);
    }
    private Context _downLoadContext;

    public DownloadWebpageTask(Context context)
    {
        super();
        _downLoadContext = context;
        mCallbac = (FinishGetDataListener)context;
    }

    @Override
    protected ResponseData doInBackground(RequestData... requestDatas) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(requestDatas[0]);
        } catch (IOException e) {
            return null;
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseData result) {
        //textView.setText(result);
        //ResponseData data = result;
        if (result == null)
        {
            Toast toast=Toast.makeText(_downLoadContext, "当前网络不可用", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        mCallbac.didGetData(result);
    }

    private ResponseData downloadUrl(RequestData requestData) throws IOException {

        ConnectivityManager connMgr = (ConnectivityManager)_downLoadContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {


            InputStream is = null;

            String title = null;
            String url = null;
            String summary = null;


            StringBuilder htmlString = new StringBuilder();


            //String requestURL = ServerSideURL.installURL();
            //System.out.println(ServerSideURL.installURL());
            JSONObject object;
            try {
                //stream = downloadUrl(urlString);

                String jsonResult = getDataFromURL(requestData.urlString);//stream.toString();


                String message;
                try {
                    object = new JSONObject(jsonResult);
                    return new ResponseData(requestData.apiKey,object);
                    //message = object.getString("message");
                } catch (Exception ex) {
                    object = null;
                    //message = "error";
                }


            }
            catch (Exception ex)
            {
                System.out.print(ex.getMessage());
                return null;
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        return null;


    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


    private String getDataFromURL(String urlString) throws IOException
    {
        URL url = new URL(urlString);
        HttpGet httpRequest = new HttpGet(urlString);
        String strResult = "";
        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }
        }
        catch(Exception ex)
        {
            String message = ex.getMessage();
        }


        return strResult;
    }



}
