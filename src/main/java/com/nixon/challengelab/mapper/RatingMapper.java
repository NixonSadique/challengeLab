package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.RatingRequest;
import com.nixon.challengelab.dto.response.RatingResponse;
import com.nixon.challengelab.model.Rating;

public class RatingMapper {
    public static Rating toRating(RatingRequest request){
        Rating rating = new Rating();
        rating.setCompleteness(request.completeness());
        rating.setCreativity(request.creativity());
        rating.setTechnicalQuality(request.technicalQuality());
        rating.setFeedback(request.feedback());
        return rating;
    }

    public static RatingResponse toRatingResponse(Rating rating){
        return new RatingResponse(
                rating.getId(),
                rating.getCreativity(),
                rating.getTechnicalQuality(),
                rating.getCompleteness(),
                rating.getFeedback(),
                UserMapper.toUserResponse(rating.getUser()),
                rating.getSubmission().getId()
        );
    }
}
