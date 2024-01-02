import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZodiacUtilsTest {

    @ParameterizedTest
    @MethodSource("getZodiacTestData")
    void getZodiacName(String zodiacName, LocalDate begin, LocalDate end) {
        for(LocalDate date = begin; !date.isAfter(end); date = date.plusDays(1) ){
            assertEquals(zodiacName, ZodiacUtils.getZodiacName(date.getDayOfMonth(), date.getMonthValue()));
        }
    }

    private static Object[][] getZodiacTestData(){
        return  new Object[][]{
                {"Овен",LocalDate.parse("2023-03-21"), LocalDate.parse("2023-04-19")},
                {"Телец",LocalDate.parse("2023-04-20"), LocalDate.parse("2023-05-20")},
                {"Близнецы",LocalDate.parse("2023-05-21"), LocalDate.parse("2023-06-20")},
                {"Рак",LocalDate.parse("2023-06-21"), LocalDate.parse("2023-07-22")},
                {"Лев",LocalDate.parse("2023-07-23"), LocalDate.parse("2023-08-22")},
                {"Дева",LocalDate.parse("2023-08-23"), LocalDate.parse("2023-09-22")},
                {"Весы",LocalDate.parse("2023-09-23"), LocalDate.parse("2023-10-22")},
                {"Скорпион",LocalDate.parse("2023-10-23"), LocalDate.parse("2023-11-21")},
                {"Стрелец",LocalDate.parse("2023-11-22"), LocalDate.parse("2023-12-21")},
                {"Козерог",LocalDate.parse("2023-12-22"), LocalDate.parse("2024-01-19")},
                {"Водолей",LocalDate.parse("2024-01-20"), LocalDate.parse("2024-02-18")},
                {"Рыбы",LocalDate.parse("2024-02-19"), LocalDate.parse("2024-03-20")},
        };

    }
}