package com.example.RestService.service;

import com.example.RestService.entity.RefreshToken;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(Long id);

    RefreshToken validateRefreshToken(String tokenId);

    void deleteByUserId(Long userId);

}
