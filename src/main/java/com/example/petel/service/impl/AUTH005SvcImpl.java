package com.example.petel.service.impl;

import com.example.petel.dto.AUTH005Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.AUTH005Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH005SvcImpl implements AUTH005Svc {

    /** PasswordEncoder */
    private final PasswordEncoder passwordEncoder;
    /** StringRedisTemplate */
    private final StringRedisTemplate redis;
    /** SqlAction */
    private final SqlAction sqlAction;
    /** GetSqlStrSrvUtil */
    private final SqlUtils sqlUtil;
    /** RP_PREFIX */
    private static final String RP_PREFIX = "RP:";
    /** NO_USER */
    private static final String NO_USER = "NO_USER";
    /** SQL_FILE */
    private static final String SQL_FILE = "AUTH005_UPDATE.sql";

    /**
     * 重設密碼
     * @param req
     * @return
     * @throws UpdateFailException
     * @throws IOException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<Object> resetPassword(Req<AUTH005Tranrq> req) throws UpdateFailException, IOException {
        log.info("---- [AUTH-005] 重設密碼 ----");

        AUTH005Tranrq tranrq = req.getTranrq();
        String token = tranrq.getToken();
        String newPassword = tranrq.getNewPassword();

        String key = RP_PREFIX + token;
        String accountId = null;

        try {
            accountId = redis.opsForValue().get(key);

            if (accountId == null) {
                log.warn("[AUTH-005] token 不存在 或 已過期");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "連結已失效或已被使用");
            }

            if (NO_USER.equals(accountId)) {
                log.info("[AUTH-005] token 對應 NO_USER 已處理");
                return new Res<>(
                        new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                        null
                );
            }

            String encode = passwordEncoder.encode(newPassword);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", accountId);
            map.put("pwd", encode);

            String sql = sqlUtil.getDynamicQuerySQL(SQL_FILE, map);
            int updated = sqlAction.update(sql, map);
            if (updated != 1) {
                log.error("[AUTH-005] 重設密碼失敗，accountId={}", accountId);
                throw new UpdateFailException("重設密碼失敗");
            }

            log.info("[AUTH-005] 重設密碼成功");
            return new Res<>(
                    new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                    null
            );
        } catch (Exception e) {
            log.error("[AUTH-005] 重設密碼失敗");
            throw new UpdateFailException("重設密碼失敗");
        } finally {
            redis.delete(key);
        }
    }
}
