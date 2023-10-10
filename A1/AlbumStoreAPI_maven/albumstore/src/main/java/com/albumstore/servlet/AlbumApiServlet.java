package com.albumstore.servlet;

import com.albumstore.bean.Album;
import com.albumstore.bean.ImageMetaData;
import com.albumstore.bean.ErrorMsg;
import com.google.gson.Gson;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/albums/*")
public class AlbumApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // // Deserialize JSON payload
            // Album album = gson.fromJson(request.getReader(), Album.class);

            // // Simple validation: Check if all required fields are present
            // if (album.getTitle() == null || album.getArtist() == null || album.getYear() == null) {
            //     sendErrorResponse(response, "Invalid request. Missing fields in the album data.");
            //     return;
            // }

            // As per instructions, we return a fixed album key
            ImageMetaData metaData = new ImageMetaData("fixedAlbumKey", "123456");  // For the sake of the stub, using a constant imageSize
            sendResponse(response, metaData);

        } catch (Exception ex) {
            sendErrorResponse(response, "Invalid request. Error parsing the album data.");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if(pathInfo == null || pathInfo.equals("/")) {
            sendErrorResponse(response, "Invalid request. Album ID is missing.");
        } else {
            // Here we return constant data from the GET as per instructions
            Album album = new Album("Never Mind The Bollocks", "Sex Pistols", "1977");
            sendResponse(response, album);
        }
    }

    private void sendResponse(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(obj));
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorMsg errorMsg = new ErrorMsg(errorMessage);
        sendResponse(response, errorMsg);
    }
}

