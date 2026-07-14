package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.response.RefreshTokenResponse;
import com.nixon.challengelab.model.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper extends Mapper<RefreshToken, RefreshTokenResponse> {

    @Override
    public RefreshTokenResponse toDto(RefreshToken refreshToken) {
        return new RefreshTokenResponse(
                refreshToken.getToken(),
                ""
        );
    }
}
