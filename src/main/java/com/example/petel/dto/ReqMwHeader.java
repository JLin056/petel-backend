package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqMwHeader implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String CODE_REGEX = "^(?:AUTH|USER|HOTEL|BOOK|REVIEW|CHAT|IMG|ADMIN|MERCH|MEDIA|NOTIFY)-(00[1-9]|0[1-9][0-9]|[1-9][0-9]{2})$";

    @NotBlank(message = "MSGID欄位不得為空")
    @Pattern(regexp = CODE_REGEX, message = "代碼格式錯誤")
    @JsonProperty("MSGID")
    private String msgID;
}
