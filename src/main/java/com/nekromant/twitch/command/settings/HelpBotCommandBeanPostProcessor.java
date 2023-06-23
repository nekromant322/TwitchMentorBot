package com.nekromant.twitch.command.settings;

import com.nekromant.twitch.command.BotCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Component
public class HelpBotCommandBeanPostProcessor implements BeanPostProcessor {
    private final List<String> listOfCustomCommand = new ArrayList<>();
    /**
     * С помощью Lombok`а (@FieldNameConstants) получаем имя приватного поля.
     * ВАЖНО!!! Аннотация '@FieldNameConstants' является экспериментальной.
     */
    private final String commandIdentifier = BotCommand.Fields.commandIdentifier;

    /**
     * Spring создаёт bean`ы, и сам их помещает в этот метод.
     * Результат выполнения этого метода можно при желании использовать в @PostConstruct методе.
     * @return Список имён кастомных команд, унаследованных от абстрактного класса BotCommand.
     */
    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (bean instanceof BotCommand) {
            findListOfBotCommandName(bean);
        }
        return bean;
    }

    public String getCustomCommand() {
        return String.join(" ", listOfCustomCommand);
    }

    public List<String> getListOfCustomCommand() {
        return listOfCustomCommand;
    }

    /**
     * Метод получает доступ к приватным полям передаваемых bean`ов, ищет среди них
     * поле 'commandIdentifier' и передаёт значение этого поля в 'listOfCustomCommand'.
     * @param bean, в иерархии наследования которого имеется абстрактный класс BotCommand.
     */
    private void findListOfBotCommandName(Object bean) {
        Field[] fields = getBotCommandClass(bean.getClass()).getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(commandIdentifier)) {
                field.setAccessible(true);
                try {
                    getListOfCustomCommand().add("!" + field.get(bean));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Мотод проходит рекурсивно по иерархии наследования вверх и ищет абстрактный класс BotCommand.
     * Необходимо в случае иерархического наследования кастомных команд.
     * @param clazz текущего bean`а.
     * @return BotCommand.class. Теперь можно вызвать приватные поля, среди которых 'commandIdentifier'.
     */
    private Class<?> getBotCommandClass(Class<?> clazz) {
        int mod = clazz.getModifiers();
        if (Modifier.isAbstract(mod) && !Modifier.isInterface(mod)) {
            return clazz;
        }
        return getBotCommandClass(clazz.getSuperclass());
    }
}