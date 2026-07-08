package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.RatingRequest;
import com.nixon.challengelab.dto.response.RatingResponse;
import com.nixon.challengelab.model.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMapper extends Mapper<Rating, RatingResponse> {

    private final UserMapper userMapper;

    public Rating toRating(RatingRequest request){
        Rating rating = new Rating();
        rating.setCompleteness(request.completeness());
        rating.setCreativity(request.creativity());
        rating.setTechnicalQuality(request.technicalQuality());
        rating.setFeedback(request.feedback());
        return rating;
    }

    public RatingResponse toDto(Rating rating){
        return new RatingResponse(
                rating.getId(),
                rating.getCreativity(),
                rating.getTechnicalQuality(),
                rating.getCompleteness(),
                rating.getFeedback(),
                userMapper.toDto(rating.getUser()),
                rating.getSubmission().getId()
        );
    }
}
