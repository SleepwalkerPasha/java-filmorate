# java-filmorate

## [Диаграмма сущность связь для Filmorate](https://dbdiagram.io/d/6410da92296d97641d87f263)

По данной ссылке можно посмотреть схему, которую я разработал для Filmorate с поддержкой бизнес логики.
Таблицы Film и User - обычные таблицы для хранения данных. Friendship - таблица для реализации связи многие ко многим.
(У одного пользователя много друзей, а у его друзей тоже много друзей). MovieLikes - такая же таблица для связи фильмов 
и пользователей по их id.(У пользователя может быть много залайканных фильмов, как и фильма может быть статистика 
о пользователях, которые его залайкали). Enum Genre и Rating - таблицы-перечисления с необходимыми значениями.


### Примеры запросов

#### Запрос на 10 самых популярных по лайкам фильмов
~~~ sql
select film.name, count(user_id)
from movie_likes 
left join film on film.id = movie_likes.film_id
group by film.name
order by count(user_id) desc
limit 10;
~~~

#### Запрос на получение общих друзей
~~~ sql
val user1 = 1
val user2 = 2

select friend_id
from friendship 
where friend_id in 
(select friend_id 
from friendship
where user_id = $user2)
and user_id = $user1;

~~~
