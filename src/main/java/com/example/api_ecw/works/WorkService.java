package com.example.api_ecw.works;

import com.example.api_ecw.works.dto.WorkRequest;
import com.example.api_ecw.works.dto.WorkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    public WorkResponse create(WorkRequest work) {
        Work newWork = new Work();

        newWork.setTitle(work.title());
        newWork.setSynopsis(work.synopsis());
        newWork.setReleaseDate(work.releaseDate());
        newWork.setTmdbId(work.tmdbId());
        newWork.setGenreIds(work.genreIds());
        newWork.setType(work.type());

        Work savedWork = workRepository.save(newWork);

        return new WorkResponse(
                savedWork.getId(),
                savedWork.getTitle(),
                savedWork.getSynopsis(),
                savedWork.getScore(),
                savedWork.getType(),
                savedWork.getGenreIds(),
                savedWork.getTmdbId(),
                savedWork.getReleaseDate(),
                savedWork.getCreatedAt()
        );
    }
}
