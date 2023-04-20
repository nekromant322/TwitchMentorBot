package com.nekromant.twitch.contoller.rest;

import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.service.TwitchCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/twitch-bot")
public class CommandsController {
    @Autowired
    private TwitchCommandService twitchCommandService;

    @GetMapping("/commands")
    public List<TwitchCommand> listCommands() {
        return twitchCommandService.getCommands();
    }

    @PostMapping("/add")
    public void create(@RequestBody @Validated TwitchCommand twitchCommand) {
        twitchCommandService.saveCommand(twitchCommand);
    }

    @GetMapping("/edit/{id}")
    public void edit(@PathVariable("id") Long id) {
        twitchCommandService.getCommand(id);
    }

    @PostMapping("/edit/{id}")
    public void saveEdit(@RequestBody TwitchCommand twitchCommand) {
        twitchCommandService.saveCommand(twitchCommand);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        twitchCommandService.deleteCommand(id);
    }
}
