package com.example.api_ecw.works;

import com.example.api_ecw.works.dto.WorkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    public WorkResponse create(Work work) {
        Work newWork = workRepository.save(work);

        return new WorkResponse(
                newWork.getId(),
                newWork.getTitle(),
                newWork.getSynopsis(),
                newWork.getScore(),
                newWork.getType(),
                newWork.getGenreIds(),
                newWork.getTmdbId(),
                newWork.getReleaseDate(),
                newWork.getCreatedAt()
        );
    }
}
