package com.example.RestService.common.record;

import java.util.Date;
import java.util.Set;

public record AuthResponseRecord( String username, String token, Date expires_at, String refresh_token, Set<String> permissions, Boolean isWalletUser) {
    
}
