package com.shopme.admin.user.export;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response,
                                  String contentType,
                                  String extension) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime date = LocalDateTime.now();
        String timeStamp = date.format(formatter);

        String fileName = "users_" + timeStamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; fileName=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

}
