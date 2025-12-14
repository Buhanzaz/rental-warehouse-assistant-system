package dev.buhanzaz.web.client;

import dev.buhanzaz.web.domain.EstimateDraft;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Optional;

@HttpExchange("/api/v1/estimate/draft")
public interface EstimateDraftClient {

    @GetMapping
    List<EstimateDraft> getAll();

    @PostMapping
    void save(@RequestBody EstimateDraft estimateDraft);

    @GetMapping("/{cabinId}")
    Optional<EstimateDraft> findById(@PathVariable String cabinId);
}

