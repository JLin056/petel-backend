package com.example.petel.model.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class AccountPrincipal  implements Serializable {

    private final String accountId;

    private final String role;

    private final Integer tokenVersion;
}
