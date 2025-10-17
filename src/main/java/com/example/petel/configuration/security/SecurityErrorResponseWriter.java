package com.example.petel.configuration.security;

import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SecurityErrorResponseWriter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeError(HttpServletResponse httpRes,
                                  int httpStatus,
                                  ReturnCodeAndDescEnum codeAndDescEnum,
                                  String message) throws IOException {
        httpRes.setStatus(httpStatus);
        httpRes.setContentType("application/json;charset=UTF-8");
        httpRes.setCharacterEncoding("UTF-8");

        ResMwHeader mwHeader = new ResMwHeader(codeAndDescEnum);

        if (message != null && !message.isBlank()) {
            mwHeader.setReturnDesc(message);
        }

        Res<Object> res = new Res<>(mwHeader, null);

        String json = mapper.writeValueAsString(res);
        httpRes.getWriter().write(json);

        log.warn("[SECURITY] {} - {}", codeAndDescEnum.getCode(), mwHeader.getReturnDesc());
    }
}
