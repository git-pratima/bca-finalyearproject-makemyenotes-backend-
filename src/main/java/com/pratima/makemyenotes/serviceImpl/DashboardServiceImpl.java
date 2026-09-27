package com.pratima.makemyenotes.serviceImpl;

import com.pratima.makemyenotes.entity.Dashboard;
import com.pratima.makemyenotes.entity.Event;
import com.pratima.makemyenotes.entity.Question;
import com.pratima.makemyenotes.entity.ToDo;
import com.pratima.makemyenotes.repository.DashboardRepository;
import com.pratima.makemyenotes.repository.QuestionRepository;
import com.pratima.makemyenotes.repository.TodoRepository;
import com.pratima.makemyenotes.repository.UserRevisionStatusRepository;
import com.pratima.makemyenotes.service.*;
import com.pratima.makemyenotes.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedbackDetailsService feedbackDetailsService;

    @Autowired
    private UserUtility utility;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRevisionStatusRepository repository;

    @Autowired
    private EventService eventService;

    @Override
    public Dashboard getDashboardData() {

        Optional<Dashboard> opDashboard = dashboardRepository.findByUserId(utility.getLoggedInUser().getId());
        Dashboard dashboard = null;
        if(opDashboard.isPresent()){
            dashboard = opDashboard.get();
            List<Question> categoryList = questionRepository.findAll();
            Map<String, Long> countMap = categoryList.stream()
                    .collect(Collectors.groupingBy(
                            Question::getSubCategory,   // key = subCategory
                            Collectors.counting()       // value = count of each subCategory
                    ));

            dashboard.setCountMap(countMap);

            List<ToDo> toDoList = todoRepository.findByUserId(utility.getLoggedInUser().getId());

            dashboard.setToDoList(toDoList);

            //<=======================================================>
            Map<LocalDate, List<ToDo>> toDoMap =
                    toDoList.stream()
                            .collect(Collectors.groupingBy(
                                    todo -> todo.getCreatedDate()
                                            .toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                            ))
                            .entrySet()
                            .stream()
                            .sorted(Map.Entry.<LocalDate, List<ToDo>>comparingByKey().reversed())
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (a, b) -> a,
                                    LinkedHashMap::new
                            ));

            dashboard.setToDoMap(toDoMap);

            //<======================================================================>
            Map<String, Long> dailyQuestionCountMap = new HashMap<>();
            dailyQuestionCountMap = questionService.getDailyQuestionCountForCurrentMonth();
            dashboard.setDailyQuestionCountMap(dailyQuestionCountMap);

            Map<String, Long> userMonthlyRevisionMap = getMonthlyReport(utility.getLoggedInUser().getId());
            dashboard.setUserMonthlyRevisionReport(userMonthlyRevisionMap);

            List<Map<String, Object>> res = getTypeStatsByUser(utility.getLoggedInUser().getId());
            dashboard.setTheorayPracticalData(res);

           List<Event> userFutureEvents = eventService.findByUserIdAndStartDateAfter(utility.getLoggedInUser().getId(), LocalDate.now());
            dashboard.setFutureEventList(userFutureEvents);
        }
        return dashboard;
    }

    public List<Map<String, Object>> getTypeStatsByUser(Long userId) {
        return repository.findTypeStatsByUserId(userId)
                .stream().map(row -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("created_date", row[0].toString());
                    map.put("type", row[1].toString());
                    map.put("count", row[2]);
                    return map;
                }).collect(Collectors.toList());
    }


    public Map<String, Long> getMonthlyReport(Long userId) {

        YearMonth yearMonth = YearMonth.now(); // 👈 current month

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault()).toInstant());

        List<Object[]> results = repository.findDailyMaxCountForMonth(userId, start, end);

        Map<LocalDate, Long> dbData = new HashMap<>();

        for (Object[] row : results) {
            Date date = (Date) row[0];
            Long maxCount = (Long) row[1];

            LocalDate localDate = ((java.sql.Date) date).toLocalDate();

            dbData.put(localDate, maxCount);
        }

        Map<String, Long> finalReport = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd");

        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            finalReport.put(current.format(formatter), dbData.getOrDefault(current, 0L));
            current = current.plusDays(1);
        }

        return finalReport;
    }

}
