import entity.Movie

import service.ConsoleUI
import service.CinemaManager

import com.fasterxml.jackson.databind.ObjectMapper
import entity.Seat
import service.CinemaHandler
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


var username = ""

fun main() {

    val cinemaHandler = CinemaHandler()

    val cinemaManager = CinemaManager(cinemaHandler)
//    cinemaManager.loadInitialData() // Загрузка данных из файлов

    val consoleUI = ConsoleUI(cinemaManager)

    // Отобразить главное меню и начать обработку ввода пользователя

    //consoleUI.handleUserInput()

    // Создаем объект Entity.Movie
    // LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString()
    val movie = Movie("Meet Joe Black", "Director")
    val movie2 = Movie("Quite Place", "Director")
    cinemaManager.addSession(
        movie,
        LocalDate.of(2004, 11, 26).toString(),
        LocalTime.of(14, 30).toString(),
        LocalTime.of(15, 50).toString()
    )
    cinemaManager.addSession(
        movie2,
        LocalDate.of(2004, 12, 26).toString(),
        LocalTime.of(15, 30).toString(),
        LocalTime.of(15, 50).toString()
    )
    cinemaManager.addSession(
        movie,
        LocalDate.of(2004, 12, 26).toString(),
        LocalTime.of(13, 30).toString(),
        LocalTime.of(16, 50).toString()
    )
    cinemaManager.addSession(
        movie,
        LocalDate.of(2004, 12, 26).toString(),
        LocalTime.of(14, 30).toString(),
        LocalTime.of(16, 50).toString()
    )

    cinemaManager.getSessionById(1)?.markSeat(1,6)
    cinemaManager.getSessionById(2)?.markSeat(2,6)
    cinemaManager.getSessionById(3)?.markSeat(3,6)
    cinemaManager.getSessionById(4)?.markSeat(4,6)

    cinemaManager.saveSessionsToFile()

    consoleUI.run()

//
//    val filePath = "Data/movie.json";
//
//    // Записываем в файл JSON
//    writeToJsonFile(movie, filePath)
//
//    // Читаем из файла JSON
//    val loadedMovie = readFromJsonFile(filePath)
//
//    // Выводим информацию о фильме на экран
//    println("Loaded Entity.Movie: $loadedMovie")
}

fun writeToJsonFile(movie: Movie, fileName: String) {
    // Создаем объект ObjectMapper
    val objectMapper = ObjectMapper()

    // Записываем объект Entity.Movie в файл JSON
    objectMapper.writeValue(File(fileName), movie)
}

fun readFromJsonFile(fileName: String): Movie {
    // Создаем объект ObjectMapper
    val objectMapper = ObjectMapper()

    // Читаем объект Entity.Movie из файла JSON
    return objectMapper.readValue(File(fileName), Movie::class.java)
}
