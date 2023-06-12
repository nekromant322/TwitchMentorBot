package com.nekromant.twitch.service;

import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.repository.TwitchCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitchCommandService {
    @Autowired
    private TwitchCommandRepository twitchCommandRepository;

    public List<TwitchCommand> getCommands() {
        return (List<TwitchCommand>) twitchCommandRepository.findAll();
    }

    public TwitchCommand getCommand(Long id) {
        if (twitchCommandRepository.findById(id).isPresent()) {
            return twitchCommandRepository.findById(id).get();
        }
        return new TwitchCommand();
    }

    public TwitchCommand getCommand(String name) {
        return twitchCommandRepository.findByName(name);
    }

    public void saveCommand(TwitchCommand command) {
        twitchCommandRepository.save(command);
    }

    public void editCommand(TwitchCommand updatedCommand) {
        TwitchCommand commandToUpdate = getCommand(updatedCommand.getId());
        commandToUpdate.setName(updatedCommand.getName());
        commandToUpdate.setResponse(updatedCommand.getResponse());
        commandToUpdate.setPeriod(updatedCommand.getPeriod());
        commandToUpdate.setEnabled(updatedCommand.isEnabled());
        twitchCommandRepository.save(commandToUpdate);
    }

    public void deleteCommand(Long id) {
        twitchCommandRepository.deleteById(id);
    }
}
