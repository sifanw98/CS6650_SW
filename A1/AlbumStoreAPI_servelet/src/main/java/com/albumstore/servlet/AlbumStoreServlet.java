package main.java.com.albumstore.servlet;

import main.java.com.albumstore.bean.Album;
import main.java.com.albumstore.bean.ApiResponse;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AlbumApiServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");        
        Gson gson = new Gson();

        try {
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = request.getReader().readLine()) != null) {
                sb.append(s);
            }

            Album album = gson.fromJson(sb.toString(), Album.class);

            ApiResponse apiResponse = new ApiResponse();
            if (album.getTitle() != null && !album.getTitle().isEmpty()) {
                apiResponse.setSuccess(true);
                apiResponse.setMessage("Album processed successfully.");
            } else {
                apiResponse.setSuccess(false);
                apiResponse.setMessage("Album title missing.");
            }
            response.getOutputStream().print(gson.toJson(apiResponse));
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setSuccess(false);
            apiResponse.setMessage(ex.getMessage());
            response.getOutputStream().print(gson.toJson(apiResponse));
            response.getOutputStream().flush();
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
