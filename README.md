### Перед запуском:

- Установи JDK 11
- Idea-> Plugins -> Lombok -> Install
- Idea -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Proccesor -> Enable annotation Proccesing

  

### Git Branching:

- Новые ветки с изменениями должны называться feature/{суть задачи} (например, feature/hello-command)
- Прежде чем начать работу над задачей :
1. git checkout main
2. git pull origin main
3. git checkout -b {имя ветки}

По завершении создать пул реквест и просить о ревью

### Добавление переменных окружения (Environment variables):

- Edit Configurations ->  Вставляем нужные значения в поле Environment variables -> Apply

*Если строка не отображается, то Modify options -> Environment variables (или Alt+E)

### Где взять значения переменных окружения:

В проекте для разных целей используются разные твич-приложения:

- Bot App - для функций чата
- Moderation App - для функций с наградами и просмотра списка модераторов
- ChatGPT App - для оценки доброты пользователей
- DonationAlerts App - для добавления доброты за донаты


Для корректного тестирования функций в своем канале советую зарегистрировать еще один Твич аккаунт и уже на нем создавать приложения.

1. По [гайду от твича](https://dev.twitch.tv/docs/authentication/register-app/) создай приложение в [консоли разработчика на твиче](https://dev.twitch.tv/console/apps):
    - При создании приложения требуется придумать redirect url. можно взять предложенный твичом - `http://localhost:3000`
    - Выпусти секретный ключ (кнопка New secret)
    - Сохрани себе клиент айди и секретный ключ, они понадобятся позднее
2. По [гайду от твича](https://dev.twitch.tv/docs/authentication/getting-tokens-oauth/) создай токен доступа и рефреш токен (раздел **Authorization code grant flow)**, используя полученные клиент айди и сикрет.
    - Для чатбота указывай скоупы chat:read и chat:edit
    - Для работы с наградами понадобятся все скоупы с moderation и moderate
    
    Все скоупы можно посмотреть [тут](https://dev.twitch.tv/docs/authentication/scopes/).
    
    Есть [другой быстрый способ выпуска токенов](https://twitchtokengenerator.com/), но в этом случае используется клиент айди токен генератора, что в будущем может привести к поломкам. 

Шаги выше необходимо повторить 2 раза: для Bot и для Moderation.

Все полученные креды добавь в переменные окружения, имена для переменных смотри в application.yml. (Пример: BOT_CLIENT_ID=hof5gwx0su6owfnys0yan9c87zr6t)

POSTGRES_PASSWORD и POSTGRES_USER можно придумать свои или взять стандартные postgres, postgres. По умолчанию POSTGRES_DB=twitch_bot.

Полученные токены (доступа и рефреш) в первый раз понадобится вручную внести в таблицу twitch_token БД, указав для чатбота type - “auth”, а для модерации - “moderation”.

3. Для работы с ChatGPT создаём secret Key https://platform.openai.com/account/api-keys, при создании копируем key - это наш API_KEY_Chat_GPT
   На момент написания туториала OpenAI не доступен на территории РФ, поэтому:
   -Используем VPN
   -Регистрируемся из под гугловской почты
   -Возможно потребуется подтверждение по номеру телефона, зарегистрированном не в РФ
   (можно использовать онлайн сервисы для подтверждения) Рекомендую https://365sms.org/ru
    
4. Вот гайд от [donationalerts] https://www.donationalerts.com/apidoc#advertisement. Для ленивых ниже инструкция.
Необходимо создать новое приложение https://www.donationalerts.com/application/clients
- имя любое 
-URL: http://localhost:3000
App ID - это твой client_id от donationalerts (Эти креды необходимо добавить в application.yml)
API Key - это твой client_secret от donationalerts (Эти креды необходимо добавить в application.yml)
Потом необходимо вставить в браузере такой запрос:
https://www.donationalerts.com/oauth/authorize?
client_id=ТВОЙ_client_id
&redirect_uri=http://localhost:3000
&response_type=code
&scope=oauth-donation-index
- необходимо подтвердить и получишь ссылку по типу:

http://localhost:3000/?code=ТВОЙ_КОД_АВТОРИЗАЦИИ
Следующим шагом необходимо в посмане собрать POST запрос(body -> x-www-form-urlencoded) по типу:
https://www.donationalerts.com/oauth/token

grant_type=authorization_code
client_id=ТВОЙ_client_id
client_secret=ТВОЙ_client_secret
redirect_uri=http://localhost:3000
code=ТВОЙ_КОД_АВТОРИЗАЦИИ

в ответ получишь json с токенами(эти токены необхожимо добавить в таблицу donation_alerts_token)

5. Для работы с YouTube:
- Создать аккаунт Google, если его нет
- Создать проект в [консоли разработчиков Google](https://console.developers.google.com/?hl=ru) и [получить API Key](https://developers.google.com/youtube/registering_an_application?hl=ru), чтобы ваше приложение могло отправлять запросы API
   1. В [консоли разработчиков Google](https://console.developers.google.com/?hl=ru) нажмите **NEW PROJECT** и заполните имя проекта на свой вкус.
   2. В [консоли разработчиков Google](https://console.developers.google.com/?hl=ru) в левом верхнем углу выберите созданным проект.
   3. Откройте [страницу "Credentials"](https://console.cloud.google.com/apis/credentials?hl=ru) в меню слева консоли API. Если меню нет, нажмите три полоски слева от лого Google Cloud.
   4. Создайте ключ API в консоли, нажав [Create credentials](https://console.cloud.google.com/apis/credentials?hl=ru) > "API Key". Это наша переменная **YOUTUBE_API_KEY**
- После создания проекта и получения API key убедитесь, что API данных YouTube является одним из сервисов, для использования которых зарегистрировано ваше приложение:
   1. Перейдите в[консоль API](https://console.cloud.google.com/?hl=ru) и выберите проект, который вы только что зарегистрировали.
   2. Посетите [страницу "Enable APIs & Services"](https://console.cloud.google.com/apis/enabled?hl=ru). Убедитесь, что в списке API для API данных YouTube v3 включен статус «ВКЛ». Для этого
      1. Нажмите вверху "ENABLE APIS AND SERVICES"
      2. В поиске найдите "YouTube Data API V3" и нажмите на результат
      3. На открывшейся странице нажмите кнопку "Enable"
- Далее нужно сконфигурировать на странице [OAuth consent screen](https://console.cloud.google.com/apis/credentials/consent?hl=ru) наше приложения
  1. Далее нужно выбрать тип юзера "External"
  2. Далее заполняем обязательные поля "App name", "User support email" и в конце в разделе Developer information "email addresses".
  3. На всех следующих шагах жмём Save and continue - ничего не меняем, оставляем поля пустыми.
  4. После того как нажмём "Back to dashboard" на странице приложения нужно нажать в разделе Testing кнопку "Publish"
- Далее получаем **YOUTUBE_CLIENT_ID** и **YOUTUBE_SECRET**:
  1. Для этого Откройте [страницу "Credentials"](https://console.cloud.google.com/apis/credentials?hl=ru) и нажмите [Create credentials](https://console.cloud.google.com/apis/credentials?hl=ru) > OAuth client ID.
  2. Выбираем тип приложения "Web application"
  3. Выбираем любое имя
  4. Жмём "Create"
  5. На [странице "Credentials"](https://console.cloud.google.com/apis/credentials?hl=ru) переходим с созданный oauth кред и в Additional information найдём наши
     - **YOUTUBE_CLIENT_ID**=Client ID
     - **YOUTUBE_SECRET**=Client secret
- Получаем **YOUTUBE_CHANNEL_ID**
   1. Для этого заходим на канал, с которого будем тянуть видео
   2. Жмём "Ещё" в описании канала под его названием
   3. "Поделиться каналом"
   4. "Скопировать идентификатор канала"


### Для разворачивания Postgres:

- Установи Docker Desktop.
- В IDEA открой docker-compose.yml. Это compose-файл, в котором указаны конфиги для контейнеров. Для запуска бд можно нажать кнопку старта рядом с сервисом twitch-bot-db. В Deploy Log должны появиться надписи наподобие: `Starting twitch-bot-db ... done`. А в Docker Desktop должна появиться группа контейнеров.

Для удобства работы с БД в IDEA есть вкладка Database, можешь использовать её, но иногда она тупит, особенно если у тебя не самый мощный комп, поэтому можно проверять работоспособность Postgres'ов вручную. Как это делается:

- Открой из Docker Desktop терминал (CLI) контейнера с нужной тебе БД.
- В CLI введи: psql -U POSTGRES_USER. Если приглашение к вводу сменилось на `postgres=#` поздравляю, вы зашли в Postgres.
- Выполни `\l` для получения списка доступных БД в контейнере, либо же сразу подключись к нужной: \c POSTGRES_DB.
- Если приглашение к вводу сменилось на `POSTGRES_DB=#`, то можешь выполнить `\dt` для получения списка таблиц или любой SQL-запрос.



В помощь для работы с приложением [документация к библиотеке Twitch4j](https://twitch4j.github.io/).
