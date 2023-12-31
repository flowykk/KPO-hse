package service.handlers

import MovieModes
import entity.Movie
import service.CinemaManager
import service.ConsoleUI
import service.util.capitalizeFirst
import service.util.isDirector
import service.util.isMovieTitle

interface MovieHandlerEntity {
    fun handleMovieInput(mode: MovieModes): Movie?
    fun editMovie(mode: MovieModes)
    fun addMovie()
    fun deleteMovie()
    fun displayMovies()
}

class MovieHandler(
    private val cinemaManager: CinemaManager,
    private val consoleUI: ConsoleUI
) : MenuEntity, MovieHandlerEntity {
    override fun run() {
        displayMenu()
        handleMenuInput()
    }

    override fun displayMenu() {
        println("1. Посмотреть фильмы в прокате")
        println("2. Добавить фильм")
        println("3. Удалить фильм")
        println("4. Изменить название фильма")
        println("5. Изменить режиссёра фильма")
        println("00. Вернуться в Главное меню")
        println("0. Выход")
    }

    override fun handleMenuInput() {
        while (true) {
            print("Введите число от 0 до 5 или 00: ")
            val userInput: String? = readlnOrNull()

            when (userInput) {
                "1" -> displayMovies()
                "2" -> addMovie()
                "3" -> deleteMovie()
                "4" -> editMovie(MovieModes.EDITNAME)
                "5" -> editMovie(MovieModes.EDITDIRECTOR)
                "00" -> {
                    println()
                    consoleUI.getMainMenuHandler.run()
                }

                "0" -> consoleUI.exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 5 или 00.")
            }

            run()
        }
    }

    /*
    mode = add - adding new film
    mode = search - searching for existing film
     */
    override fun handleMovieInput(mode: MovieModes): Movie? {
        var movie: Movie? = null

        while (true) {
            print("Введите название фильма: ")
            var title: String = readlnOrNull().orEmpty().trim().lowercase()
            if (title == "00") {
                break
            } else if (title.isEmpty() || !isMovieTitle(title)) {
                println(
                    "Название фильма введено некорректно!\n" +
                            "Повторите ввод ещё раз или введите 00 для выхода в меню."
                )
                continue
            }

            movie = cinemaManager.getMovieByName(title)
            if (((movie == null) && mode == MovieModes.SEARCH) || ((movie != null) && mode == MovieModes.ADD)) {
                println(
                    "Фильм \"$title\" ${if (mode == MovieModes.SEARCH) "пока что отсутсвует" else "уже присутсвует"} в прокате!\n" +
                            "Повторите ввод ещё раз или введите 00 для выхода в меню."
                )
            } else {
                if ((movie == null) && mode == MovieModes.ADD) {
                    var director: String

                    while (true) {
                        print("Введите режиссёра фильма: ")
                        director = readlnOrNull().orEmpty().trim().lowercase()

                        if (director == "00") {
                            return null
                        }

                        if (!isDirector(director)) {
                            println(
                                "Режиссёр введён некорректно!\n" +
                                        "Повторите ввод ещё раз или введите 00 для выхода в меню."
                            )
                        } else {
                            break
                        }
                    }

                    director = capitalizeFirst(director)
                    title = capitalizeFirst(title)

                    movie = Movie(title, director)
                }

                break
            }
        }

        return movie
    }

    override fun editMovie(mode: MovieModes) {
        displayMovies()

        println("Введите информацию о фильме для изменения: ")
        val movie = handleMovieInput(MovieModes.SEARCH) ?: return

        var data: String

        while (true) {
            print("Введите ${if (mode == MovieModes.EDITNAME) "новое название" else "нового режиссёра"} фильма: ")
            data = readlnOrNull().orEmpty().trim().lowercase()

            if (data == "00") {
                break
            }

            if (!isDirector(data)) {
                println(
                    "${if (mode == MovieModes.EDITNAME) "Название фильма введено" else "Режиссёр введён"} некорректно!\n" +
                            "Повторите ввод ещё раз или введите 00 для выхода в меню."
                )
            } else {
                break
            }
        }

        if (cinemaManager.editMovie(movie, mode, data)) {
            println("Изменения произошли успешно!\n")
        } else {
            println("Изменения не могут быть произведены! Возможно, Вы пытаетесь изменить данные на оригинальные.")
        }
    }

    override fun addMovie() {
        displayMovies()

        val movie = handleMovieInput(MovieModes.ADD) ?: return
        cinemaManager.addMovie(movie)
    }

    override fun deleteMovie() {
        displayMovies()

        val movie = handleMovieInput(MovieModes.SEARCH) ?: return
        cinemaManager.deleteMovie(movie)
    }

    override fun displayMovies() {
        val movies = cinemaManager.getMovies()
        if (movies.isEmpty()) {
            println("Сейчас в прокате нету фильмов")
            return
        }

        println("\nСписок фильмов, которые сейчас в прокате:")
        for (movie in movies) movie.viewInfo()

        println()
    }
}