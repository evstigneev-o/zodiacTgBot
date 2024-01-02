import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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
                {"Овен",LocalDate.parse("2024-03-21"), LocalDate.parse("2024-04-19")},
                {"Телец",LocalDate.parse("2024-04-20"), LocalDate.parse("2024-05-20")},
                {"Близнецы",LocalDate.parse("2024-05-21"), LocalDate.parse("2024-06-20")},
                {"Рак",LocalDate.parse("2024-06-21"), LocalDate.parse("2024-07-22")},
                {"Лев",LocalDate.parse("2024-07-23"), LocalDate.parse("2024-08-22")},
                {"Дева",LocalDate.parse("2024-08-23"), LocalDate.parse("2024-09-22")},
                {"Весы",LocalDate.parse("2024-09-23"), LocalDate.parse("2024-10-22")},
                {"Скорпион",LocalDate.parse("2024-10-23"), LocalDate.parse("2024-11-21")},
                {"Стрелец",LocalDate.parse("2024-11-22"), LocalDate.parse("2024-12-21")},
                {"Козерог",LocalDate.parse("2024-12-22"), LocalDate.parse("2024-01-19")},
                {"Водолей",LocalDate.parse("2024-01-20"), LocalDate.parse("2024-02-18")},
                {"Рыбы",LocalDate.parse("2024-02-19"), LocalDate.parse("2024-03-20")},
        };

    }
}