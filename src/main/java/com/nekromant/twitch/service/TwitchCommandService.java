package com.nekromant.twitch.service;

import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.repository.TwitchCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TwitchCommandService {
    @Autowired
    private TwitchCommandRepository twitchCommandRepository;

    public List<TwitchCommand> getCommands() {
        return (List<TwitchCommand>) twitchCommandRepository.findAll();
    }

    public Optional<TwitchCommand> getCommand(Long id) {
        return twitchCommandRepository.findById(id);
    }

    public void saveCommand(TwitchCommand command) {
        twitchCommandRepository.save(command);
    }

    public void editCommand(TwitchCommand command) {

    }

    public void deleteCommand(Long id) {
        twitchCommandRepository.deleteById(id);
    }
}
