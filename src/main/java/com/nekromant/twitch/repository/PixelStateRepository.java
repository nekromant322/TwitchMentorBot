package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.PixelState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PixelStateRepository extends CrudRepository<PixelState, Long> {
}
