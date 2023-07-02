package com.nekromant.twitch.command.settings;

import com.nekromant.twitch.command.BotCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelpBotCommandBeanPostProcessor implements BeanPostProcessor {
    private final List<String> listOfCustomCommand = new ArrayList<>();
    /**
     * Spring создаёт bean`ы, и сам их помещает в этот метод.
     * Результат выполнения этого метода можно при желании использовать в @PostConstruct методе.
     * @return Список имён кастомных команд, унаследованных от абстрактного класса BotCommand.
     */
    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (bean instanceof BotCommand) {
            getListOfCustomCommand().add("!" + ((BotCommand) bean).getCommandIdentifier());
        }
        return bean;
    }

    public String getCustomCommand() {
        return String.join(" ", listOfCustomCommand);
    }

    public List<String> getListOfCustomCommand() {
        return listOfCustomCommand;
    }
}